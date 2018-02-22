package com.mining.profit.pool.account;

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
     * Enumeration of pool account executor type.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum AccountExecutorType {
        DWARFPOOL
    }

    /**
     * Returns pool account executor.
     *
     * @param type type of pool account executor
     * @return pool account executor
     */
    public static AccountExecutor getAccountExecutor(AccountExecutorType type) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (type) {
        case DWARFPOOL: {
            return new DwarfpoolAccountExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(type.name());
        }
    }

}
