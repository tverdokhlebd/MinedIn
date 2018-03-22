package com.mined.in.pool.account;

import com.mined.in.pool.PoolType;
import com.mined.in.pool.account.dwarfpool.DwarfpoolAccountExecutor;
import com.mined.in.pool.account.ethermine.EthermineAccountExecutor;
import com.mined.in.pool.account.nanopool.NanopoolAccountExecutor;

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
    public static AccountExecutor getAccountExecutor(PoolType pool) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (pool) {
        case DWARFPOOL: {
            return new DwarfpoolAccountExecutor(httpClient);
        }
        case ETHERMINE: {
            return new EthermineAccountExecutor(httpClient);
        }
        case NANOPOOL: {
            return new NanopoolAccountExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(pool.name());
        }
    }

}
