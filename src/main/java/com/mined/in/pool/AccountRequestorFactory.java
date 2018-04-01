package com.mined.in.pool;

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
     * @param pool pool name
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType pool) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (pool) {
        case DWARFPOOL: {
            return new DwarfpoolAccountRequestor(httpClient);
        }
        case ETHERMINE: {
            return new EthermineAccountRequestor(httpClient);
        }
        case NANOPOOL: {
            return new NanopoolAccountRequestor(httpClient);
        }
        default:
            throw new IllegalArgumentException(pool.name());
        }
    }

}
