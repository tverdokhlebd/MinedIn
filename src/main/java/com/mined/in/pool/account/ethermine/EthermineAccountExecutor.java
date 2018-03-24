package com.mined.in.pool.account.ethermine;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of Ethermine executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthermineAccountExecutor implements AccountExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Ethermine API url. */
    private static final String API_URL = "https://api.ethermine.org/miner/:miner/currentStats";
    /** Wei. */
    private final static BigDecimal WEI = new BigDecimal("1000000000000000000");

    /**
     * Creates the Ethermine executor instance.
     *
     * @param httpClient HTTP client
     */
    public EthermineAccountExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Account getETHAccount(String walletAddress) throws AccountExecutorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountExecutorException(API_ERROR, "BAD_WALLET");
        }
        Request request = new Request.Builder().url(API_URL.replace(":miner", walletAddress)).build();
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
     * Creates account from JSON response.
     *
     * @param walletAddress wallet address
     * @param jsonAccount account in JSON format
     */
    private Account createAccount(String walletAddress, JSONObject jsonAccount) {
        BigDecimal walletBalance = new BigDecimal(0);
        JSONObject data = jsonAccount.optJSONObject("data");
        if (data != null) {
            Long balanceLong = data.getLong("unpaid");
            walletBalance = BigDecimal.valueOf(balanceLong);
            walletBalance = walletBalance.divide(WEI);
            walletBalance = walletBalance.stripTrailingZeros();
        }
        return new Account(walletAddress, walletBalance);
    }

    /**
     * Check presence of error in JSON response.
     *
     * @param jsonResponse response in JSON format
     * @throws AccountExecutorException if there is any error in JSON response
     */
    private void checkError(JSONObject jsonResponse) throws AccountExecutorException {
        String status = jsonResponse.getString("status");
        if (status.equalsIgnoreCase("error")) {
            String errorMessage = jsonResponse.getString("error");
            throw new AccountExecutorException(API_ERROR, errorMessage);
        }
    }

}
