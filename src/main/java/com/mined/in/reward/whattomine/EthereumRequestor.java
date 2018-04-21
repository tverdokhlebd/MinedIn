package com.mined.in.reward.whattomine;

import okhttp3.OkHttpClient;

/**
 * Requestor of ethereum estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class EthereumRequestor extends Requestor {

    /** API url. */
    private static final String API_URL = "https://whattomine.com/coins/151.json";

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    EthereumRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super(httpClient, endpointsUpdate);
    }

    @Override
    public String getUrl() {
        return API_URL;
    }

}
