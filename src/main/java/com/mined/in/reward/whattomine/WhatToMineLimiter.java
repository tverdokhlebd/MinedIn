package com.mined.in.reward.whattomine;

import com.mined.in.okhttp.OkHttpLimiterInterceptor;

/**
 * Limiter of HTTP requests for WhatToMine.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineLimiter extends OkHttpLimiterInterceptor {

    /** Limiter. */
    private static OkHttpLimiterInterceptor INSTANCE = new WhatToMineLimiter();

    /**
     * Creates the instance.
     */
    public WhatToMineLimiter() {
        super(1, 7 * 1000);
    }

    /**
     * Gets the instance of WhatToMine limiter.
     *
     * @return the instance of WhatToMine limiter
     */
    public static OkHttpLimiterInterceptor get() {
        return INSTANCE;
    }

}