package com.mining.profit.pool.account.dwarfpool;

import java.io.IOException;

import org.json.JSONObject;

import com.mining.profit.pool.account.Account;
import com.mining.profit.pool.account.AccountException;
import com.mining.profit.pool.account.AccountExecutor;
import com.mining.profit.pool.account.AccountExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Dwarfpool executor (https://dwarfpool.com/).
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
    public Account getETHAccount(String walletAddress) throws AccountException, AccountExecutorException {
        Request request = new Request.Builder().url(API_URL + walletAddress).build();
        DwarfpoolAccount account = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountExecutorException(response.code());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            account = DwarfpoolAccount.create(jsonResponse);
        } catch (IOException e) {
            throw new AccountExecutorException(e);
        }
        return account;
    }

}
