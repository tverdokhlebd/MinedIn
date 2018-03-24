package com.mined.in.pool.account.ethermine;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;

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
        EthermineAccount account = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                account = EthermineAccount.create(walletAddress, jsonResponse);
            }
        } catch (JSONException e) {
            throw new AccountExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountExecutorException(HTTP_ERROR, e);
        }
        return account;
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
