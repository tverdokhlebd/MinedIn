package com.mined.in.pool.account.nanopool;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.Worker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of Nanopool executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class NanopoolAccountExecutor implements AccountExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Nanopool API balance url. */
    private static final String API_BALANCE_URL = "https://api.nanopool.org/v1/eth/balance/";
    /** Nanopool API workers url. */
    private static final String API_WORKERS_URL = "https://api.nanopool.org/v1/eth/reportedhashrates/";

    /**
     * Creates the Nanopool executor instance.
     *
     * @param httpClient HTTP client
     */
    public NanopoolAccountExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Account getETHAccount(String walletAddress) throws AccountExecutorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountExecutorException(API_ERROR, "BAD_WALLET");
        }
        Account account = createAccountWithBalance(walletAddress);
        addWorkerListToAccount(account);
        return account;
    }

    /**
     * Creates account with balance.
     *
     * @param walletAddress wallet address
     * @return account with balance
     * @throws AccountExecutorException if there is any error in account creating
     */
    private Account createAccountWithBalance(String walletAddress) throws AccountExecutorException {
        Request request = new Request.Builder().url(API_BALANCE_URL + walletAddress).build();
        Account account = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                account = createAccount(walletAddress, jsonResponse);
            }
        } catch (JSONException e) {
            throw new AccountExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountExecutorException(HTTP_ERROR, e);
        }
        return account;
    }

    /**
     * Adds worker list to existing account.
     *
     * @param account created account
     * @throws AccountExecutorException if there is any error in account creating
     */
    private void addWorkerListToAccount(Account account) throws AccountExecutorException {
        Request request = new Request.Builder().url(API_WORKERS_URL + account.getWalletAddress()).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                List<Worker> workerList = getWorkerList(jsonResponse);
                account.setWorkerList(workerList);
                account.setTotalHashrate(workerList.stream()
                                                   .map(t -> t.getHashrate())
                                                   .collect(Collectors.summarizingDouble(Double::doubleValue))
                                                   .getSum());
            }
        } catch (JSONException e) {
            throw new AccountExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountExecutorException(HTTP_ERROR, e);
        }
    }

    /**
     * Creates account from JSON response.
     *
     * @param walletAddress wallet address
     * @param jsonAccount account in JSON format
     */
    private Account createAccount(String walletAddress, JSONObject jsonAccount) {
        BigDecimal walletBalance = BigDecimal.valueOf(jsonAccount.getDouble("data"));
        walletBalance = walletBalance.stripTrailingZeros();
        return new Account(walletAddress, walletBalance, 0, null);
    }

    /**
     * Returns list of workers.
     *
     * @param jsonWorker workers in JSON format
     * @return list of workers
     */
    private List<Worker> getWorkerList(JSONObject jsonWorker) {
        JSONArray workerArray = jsonWorker.getJSONArray("data");
        List<Worker> workerList = new ArrayList<>(workerArray.length());
        for (int i = 0; i < workerArray.length(); i++) {
            JSONObject worker = workerArray.getJSONObject(i);
            String workerName = worker.getString("worker");
            double workerHashrate = worker.getDouble("hashrate");
            workerList.add(new Worker(workerName, workerHashrate));
        }
        return workerList;
    }

    /**
     * Check presence of error in JSON response.
     *
     * @param jsonResponse response in JSON format
     * @throws AccountExecutorException if there is any error in JSON response
     */
    private void checkError(JSONObject jsonResponse) throws AccountExecutorException {
        boolean status = jsonResponse.getBoolean("status");
        if (!status) {
            String errorMessage = jsonResponse.getString("error");
            throw new AccountExecutorException(API_ERROR, errorMessage);
        }
    }

}
