package com.mined.in.okhttp;

import static okhttp3.Protocol.HTTP_2;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Limiter of HTTP requests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class OkHttpLimiterInterceptor implements Interceptor {

    /** Counter of HTTP requests. */
    private final AtomicInteger requestCounter;
    /** Delay in milliseconds before counter is to be reseted. */
    private final int resetCounterDelay;
    /** Maximum number of HTTP requests. */
    private final int maxRequestCount;
    /** Scheduler for counter reset. */
    private final Timer scheduler;
    /** Cached response body. */
    private String cachedResponseBody;
    /** Cached response message. */
    private String cachedResponseMessage;
    /** Cached response code. */
    private int cachedResponseCode;
    /** Cached content type. */
    private MediaType cachedContentType;

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
        this.resetCounterDelay = resetCounterDelay;
        this.scheduler = new Timer(true);
        schedule();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if (requestCounter.get() >= maxRequestCount) {
            Request request = chain.request();
            response = createResponse(request);
        } else {
            if (requestCounter.incrementAndGet() <= maxRequestCount) {
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
                try (ResponseBody body = originalResponse.body()) {
                    cachedResponseBody = body.string();
                    cachedContentType = body.contentType();
                    cachedResponseMessage = originalResponse.message();
                    cachedResponseCode = originalResponse.code();
                }
                response = createResponse(request);
            }
        }
        return response;
    }

    /**
     * Returns created response from the cache.
     *
     * @param request HTTP request
     * @return created response from the cache
     */
    private Response createResponse(Request request) {
        ResponseBody cachedBody = ResponseBody.create(cachedContentType, cachedResponseBody);
        return new Response.Builder().body(cachedBody)
                                     .request(request)
                                     .protocol(HTTP_2)
                                     .code(cachedResponseCode)
                                     .message(cachedResponseMessage)
                                     .build();
    }

    /**
     * Schedules reset of counter.
     */
    private void schedule() {
        scheduler.schedule(new TimerTask() {

            @Override
            public void run() {
                requestCounter.set(0);
                schedule();
            }

        }, resetCounterDelay);
    }

}
