package com.mined.in.market.coinmarketcap;

import okhttp3.OkHttpClient;

/**
 * Requestor of ethereum classic coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class EthereumClassicRequestor extends Requestor {

    /** API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/ethereum-classic";

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    EthereumClassicRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super(httpClient, endpointsUpdate);
    }

    @Override
    public String getUrl() {
        return API_URL;
    }

}
