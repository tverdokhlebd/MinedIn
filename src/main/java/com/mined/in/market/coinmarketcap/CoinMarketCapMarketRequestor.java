package com.mined.in.market.coinmarketcap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;

import okhttp3.OkHttpClient;

/**
 * CoinMarketCap market requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarketCapMarketRequestor implements MarketRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Ethereum coin market lock. */
    private static final Lock ETHEREUM_LOCK = new ReentrantLock();
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
    public CoinMarket requestEthereumCoin() throws MarketRequestorException {
        ETHEREUM_LOCK.lock();
        try {
            return new EthereumRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            ETHEREUM_LOCK.unlock();
        }
    }

}
