package com.mined.in.market.coinmarketcap;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for CoinMarketCap.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarketCapLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new CoinMarketCapLimiter();

    /**
     * Creates the instance of CoinMarketCap limiter.
     */
    public CoinMarketCapLimiter() {
        super(1, 7 * 1000);
    }

    /**
     * Returns the instance of CoinMarketCap limiter.
     *
     * @return the instance of CoinMarketCap limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}