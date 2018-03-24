package com.mined.in.market;

import com.mined.in.market.coinmarketcap.CoinMarketCapLimiter;
import com.mined.in.market.coinmarketcap.CoinMarketCapMarketExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of market executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MarketExecutorFactory {

    /**
     * Returns market executor.
     *
     * @param marketType market type
     * @return market executor
     */
    public static MarketExecutor getMarketExecutor(MarketType marketType) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (marketType) {
        case COIN_MARKET_CAP: {
            okHttpBuilder.addInterceptor(CoinMarketCapLimiter.get());
            return new CoinMarketCapMarketExecutor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(marketType.name());
        }
    }

}
