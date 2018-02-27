package com.mining.profit.exchanger.currencypair;

import com.mining.profit.exchanger.Exchanger;
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
     * Returns currency pair executor.
     *
     * @param exchanger exchanger name
     * @return currency pair executor
     */
    public static CurrencyPairExecutor getCurrencyPairExecutor(Exchanger exchanger) {
        OkHttpClient httpClient = new OkHttpClient();
        switch (exchanger) {
        case EXMO: {
            return new ExmoCurrencyPairExecutor(httpClient);
        }
        default:
            throw new IllegalArgumentException(exchanger.name());
        }
    }

}
