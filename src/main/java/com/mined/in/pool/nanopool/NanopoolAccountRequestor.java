package com.mined.in.pool.nanopool;

import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;

import okhttp3.OkHttpClient;

/**
 * Nanopool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class NanopoolAccountRequestor implements AccountRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Use accounts caching or not. */
    private final boolean useAccountCaching;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public NanopoolAccountRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = true;
    }

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     */
    public NanopoolAccountRequestor(OkHttpClient httpClient, boolean useAccountCaching) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = useAccountCaching;
    }

    @Override
    public Account requestEthereumAccount(String walletAddress) throws AccountRequestorException {
        return new EthereumRequestor(httpClient, useAccountCaching).request(walletAddress);
    }

}
