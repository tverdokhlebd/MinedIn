package com.mined.in.market.coinmarketcap;

import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;

import okhttp3.OkHttpClient;

/**
 * Implementation of CoinMarketCap requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarketCapMarketRequestor implements MarketRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** ETH coin market lock. */
    private static final ReentrantLock ETH_LOCK = new ReentrantLock();
    /** Endpoints update. */
    private static final int ENDPOINTS_UPDATE = 6;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public CoinMarketCapMarketRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public CoinMarket getETHCoin() throws MarketRequestorException {
        ETH_LOCK.lock();
        try {
            return new ETHCoinMarketRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            ETH_LOCK.unlock();
        }
    }

}
