package com.mined.in.exchanger.pair.exmo;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for Exmo.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ExmoLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new ExmoLimiter();

    /**
     * Creates the instance of Exmo limiter.
     */
    private ExmoLimiter() {
        super(180, 60 * 1000);
    }

    /**
     * Returns the instance of Exmo limiter.
     *
     * @return the instance of Exmo limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}
