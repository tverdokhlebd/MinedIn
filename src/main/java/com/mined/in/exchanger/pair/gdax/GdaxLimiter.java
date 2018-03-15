package com.mined.in.exchanger.pair.gdax;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for Gdax.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class GdaxLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new GdaxLimiter();

    /**
     * Creates the instance of Gdax limiter.
     */
    private GdaxLimiter() {
        super(3, 1000);
    }

    /**
     * Returns the instance of Gdax limiter.
     *
     * @return the instance of Gdax limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}
