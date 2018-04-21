package com.mined.in.pool;

import com.mined.in.http.HttpClientFactory;
import com.mined.in.pool.dwarfpool.DwarfpoolAccountRequestor;
import com.mined.in.pool.ethermine.EthermineAccountRequestor;
import com.mined.in.pool.nanopool.NanopoolAccountRequestor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating pool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountRequestorFactory {

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType) {
        OkHttpClient httpClient = HttpClientFactory.create();
        return create(poolType, httpClient);
    }

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @param httpClient HTTP client
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType, OkHttpClient httpClient) {
        return create(poolType, httpClient, true);
    }

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType, OkHttpClient httpClient, boolean useAccountCaching) {
        switch (poolType) {
        case DWARFPOOL: {
            return new DwarfpoolAccountRequestor(httpClient, useAccountCaching);
        }
        case ETHERMINE: {
            return new EthermineAccountRequestor(httpClient, useAccountCaching);
        }
        case NANOPOOL: {
            return new NanopoolAccountRequestor(httpClient, useAccountCaching);
        }
        default:
            throw new IllegalArgumentException(poolType.name());
        }
    }

}
