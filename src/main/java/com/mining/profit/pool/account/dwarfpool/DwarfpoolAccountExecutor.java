package com.mining.profit.pool.account.dwarfpool;

import java.io.IOException;

import org.json.JSONObject;

import com.mining.profit.pool.account.PoolAccount;
import com.mining.profit.pool.account.PoolAccountException;
import com.mining.profit.pool.account.PoolAccountExecutor;
import com.mining.profit.pool.account.PoolAccountExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Dwarfpool executor (https://dwarfpool.com/).
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class DwarfpoolAccountExecutor implements PoolAccountExecutor {

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
    public PoolAccount getETHAccount(String walletAddress) throws PoolAccountException, PoolAccountExecutorException {
        Request request = new Request.Builder().url(API_URL + walletAddress).build();
        DwarfpoolAccount account = null;
        try (Response response = httpClient.newCall(request).execute()) {
            JSONObject jsonResponse = new JSONObject(response.body().toString());
            account = DwarfpoolAccount.create(jsonResponse);
        } catch (IOException e) {
            throw new PoolAccountExecutorException(e);
        }
        return account;
    }

}
