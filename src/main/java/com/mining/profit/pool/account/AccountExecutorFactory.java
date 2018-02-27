package com.mining.profit.pool.account;

import com.mining.profit.pool.Pool;
import com.mining.profit.pool.account.dwarfpool.DwarfpoolAccountExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of pool account executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountExecutorFactory {

    /**
     * Returns pool account executor.
     *
     * @param pool pool name
     * @return pool account executor
     */
    public static AccountExecutor getAccountExecutor(Pool pool) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (pool) {
        case DWARFPOOL: {
            return new DwarfpoolAccountExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(pool.name());
        }
    }

}
