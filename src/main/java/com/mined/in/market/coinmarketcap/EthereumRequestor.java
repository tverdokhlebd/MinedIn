package com.mined.in.market.coinmarketcap;

import okhttp3.OkHttpClient;

/**
 * Requestor of ethereum coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class EthereumRequestor extends Requestor {

    /** API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/ethereum";

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
