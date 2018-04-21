package com.mined.in.market;

import com.mined.in.http.HttpClientFactory;
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
        OkHttpClient httpClient = HttpClientFactory.create();
        return create(marketType, httpClient);
    }

    /**
     * Creates market requestor.
     *
     * @param marketType market type
     * @param httpClient HTTP client
     * @return market requestor
     */
    public static MarketRequestor create(MarketType marketType, OkHttpClient httpClient) {
        switch (marketType) {
        case COIN_MARKET_CAP: {
            return new CoinMarketCapMarketRequestor(httpClient);
        }
        default:
            throw new IllegalArgumentException(marketType.name());
        }
    }

}
