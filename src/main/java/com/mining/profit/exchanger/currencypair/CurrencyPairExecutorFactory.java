package com.mining.profit.exchanger.currencypair;

import com.mining.profit.exchanger.currencypair.exmo.ExmoCurrencyPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of currency pair executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CurrencyPairExecutorFactory {

    /**
     * Enumeration of currency pair executor type.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum CurrencyPairExecutorType {
        EXMO
    }

    /**
     * Returns currency pair executor.
     *
     * @param type type of currency pair executor
     * @return currency pair executor
     */
    public static CurrencyPairExecutor getCurrencyPairExecutor(CurrencyPairExecutorType type) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (type) {
        case EXMO: {
            return new ExmoCurrencyPairExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(type.name());
        }
    }

}
