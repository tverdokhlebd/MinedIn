package com.mined.in.okhttp;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Limiter of HTTP requests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class OkHttpLimiterInterceptor implements Interceptor {

    /** Counter of HTTP requests. */
    private final AtomicInteger requestCounter;
    /** Maximum number of HTTP requests. */
    private final int maxRequestCount;
    /** Cached response. */
    private Response cachedResponse;

    /**
     * Creates the instance of limiter.
     *
     * @param maxRequestCount maximum number of HTTP requests
     * @param resetCounterDelay delay in milliseconds before counter is to be reseted
     */
    public OkHttpLimiterInterceptor(int maxRequestCount, int resetCounterDelay) {
        super();
        this.requestCounter = new AtomicInteger(0);
        this.maxRequestCount = maxRequestCount;
        new Timer(true).schedule(new TimerTask() {

            @Override
            public void run() {
                requestCounter.set(0);
            }

        }, resetCounterDelay);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // TODO It is necessary to improve caching (Possibly cachedResponse can be null)
        if (requestCounter.get() >= maxRequestCount) {
            return cachedResponse;
        } else {
            if (requestCounter.incrementAndGet() <= maxRequestCount) {
                Request request = chain.request();
                cachedResponse = chain.proceed(request);
            }
        }
        return cachedResponse;
    }

}
