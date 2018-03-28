package com.mined.in.pool.nanopool;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.Account;
import com.mined.in.pool.AccountExecutor;
import com.mined.in.pool.AccountExecutorException;

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
    /** Nanopool API reported hashrate url. */
    private static final String API_HASHRATE_URL = "https://api.nanopool.org/v1/eth/reportedhashrate/";

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
        setReportedHashrate(account);
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
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                return createAccount(walletAddress, jsonResponse);
            }
        } catch (JSONException e) {
            throw new AccountExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountExecutorException(HTTP_ERROR, e);
        }
    }

    /**
     * Sets reported hashrate to existing account.
     *
     * @param account created account
     * @throws AccountExecutorException if there is any error in account creating
     */
    private void setReportedHashrate(Account account) throws AccountExecutorException {
        Request request = new Request.Builder().url(API_HASHRATE_URL + account.getWalletAddress()).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                account.setTotalHashrate(BigDecimal.valueOf(jsonResponse.getDouble("data")));
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
     * @return account from JSON response
     */
    private Account createAccount(String walletAddress, JSONObject jsonAccount) {
        BigDecimal walletBalance = BigDecimal.valueOf(jsonAccount.getDouble("data"));
        return new Account(walletAddress, walletBalance, null);
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
