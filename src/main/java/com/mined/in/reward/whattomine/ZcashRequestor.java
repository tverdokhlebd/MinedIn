package com.mined.in.reward.whattomine;

import okhttp3.OkHttpClient;

/**
 * Requestor of zcash estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ZcashRequestor extends BaseRequestor {

    /** API url. */
    private static final String API_URL = "https://whattomine.com/coins/166.json";

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    public ZcashRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super(httpClient, endpointsUpdate);
    }

    @Override
    public String getApiUrl() {
        return API_URL;
    }

}
