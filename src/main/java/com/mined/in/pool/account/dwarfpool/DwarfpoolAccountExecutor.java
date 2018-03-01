package com.mined.in.pool.account.dwarfpool;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Dwarfpool executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class DwarfpoolAccountExecutor implements AccountExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Dwarfpool API url. */
    private static final String API_URL = "http://dwarfpool.com/eth/api?wallet=";

    /**
     * Creates the Dwarfpool executor instance.
     *
     * @param httpClient HTTP client
     */
    public DwarfpoolAccountExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Account getETHAccount(String walletAddress) throws AccountExecutorException {
        Request request = new Request.Builder().url(API_URL + walletAddress).build();
        DwarfpoolAccount account = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(AccountExecutorException.ErrorCode.HTTP_ERROR, response.message());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            checkError(jsonResponse);
            account = DwarfpoolAccount.create(jsonResponse);
        } catch (JSONException e) {
            throw new AccountExecutorException(AccountExecutorException.ErrorCode.JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountExecutorException(AccountExecutorException.ErrorCode.HTTP_ERROR, e);
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
        boolean error = jsonResponse.getBoolean("error");
        if (error) {
            String errorCode = jsonResponse.getString("error_code");
            throw new AccountExecutorException(AccountExecutorException.ErrorCode.API_ERROR, errorCode);
        }
    }

}
