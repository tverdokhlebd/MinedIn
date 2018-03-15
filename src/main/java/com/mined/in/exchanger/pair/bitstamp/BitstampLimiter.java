package com.mined.in.exchanger.pair.bitstamp;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for Bitstamp.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BitstampLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new BitstampLimiter();

    /**
     * Creates the instance of Bitstamp limiter.
     */
    private BitstampLimiter() {
        super(600, 10 * 60 * 1000);
    }

    /**
     * Returns the instance of Bitstamp limiter.
     *
     * @return the instance of Bitstamp limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}
