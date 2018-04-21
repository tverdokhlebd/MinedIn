package com.mined.in.http;

import okhttp3.OkHttpClient;

/**
 * Factory for creating HTTP client.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class HttpClientFactory {

    /**
     * Creates HTTP client.
     *
     * @return HTTP client
     */
    public static OkHttpClient create() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

}
