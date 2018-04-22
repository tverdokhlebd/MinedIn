package com.mined.in.market.coinmarketcap;

import java.util.Date;

import com.mined.in.coin.CoinMarket;

import okhttp3.OkHttpClient;

/**
 * Requestor of bitcoin coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class BitcoinRequestor extends Requestor {

    /** Next update of coin market. */
    private static Date NEXT_UPDATE = new Date(0);
    /** Cached coin market. */
    private static CoinMarket COIN_MARKET;
    /** API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/bitcoin";

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    BitcoinRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super(httpClient, endpointsUpdate);
    }

    @Override
    public String getUrl() {
        return API_URL;
    }

    @Override
    public Date getCachedNextUpdate() {
        return NEXT_UPDATE;
    }

    @Override
    public void setCachedNextUpdate(Date nextUpdate) {
        NEXT_UPDATE = nextUpdate;
    }

    @Override
    public CoinMarket getCachedCoinMarket() {
        return COIN_MARKET;
    }

    @Override
    public void setCachedCoinMarket(CoinMarket coinMarket) {
        COIN_MARKET = coinMarket;
    }

}
