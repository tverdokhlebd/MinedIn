package com.mined.in.exchanger.pair.bitfinex;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for Bitfinex.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BitfinexLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new BitfinexLimiter();

    /**
     * Creates the instance of Bitfinex limiter.
     */
    private BitfinexLimiter() {
        super(90, 60 * 1000);
    }

    /**
     * Returns the instance of Bitfinex limiter.
     *
     * @return the instance of Bitfinex limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}
