package com.mined.in.market;

import com.mined.in.market.coinmarketcap.CoinMarketCapMarketRequestor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating market requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MarketRequestorFactory {

    /**
     * Creates market requestor.
     *
     * @param marketType market type
     * @return market requestor
     */
    public static MarketRequestor create(MarketType marketType) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (marketType) {
        case COIN_MARKET_CAP: {
            return new CoinMarketCapMarketRequestor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(marketType.name());
        }
    }

}
