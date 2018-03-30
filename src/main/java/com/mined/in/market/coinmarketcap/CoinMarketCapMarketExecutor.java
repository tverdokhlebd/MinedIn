package com.mined.in.market.coinmarketcap;

import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;

import okhttp3.OkHttpClient;

/**
 * Implementation of CoinMarketCap executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarketCapMarketExecutor implements MarketExecutor {

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
    public CoinMarketCapMarketExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public CoinMarket getETHCoin() throws MarketExecutorException {
        ETH_LOCK.lock();
        try {
            return new ETHCoinMarketRequestor().request(ENDPOINTS_UPDATE, httpClient);
        } finally {
            ETH_LOCK.unlock();
        }
    }

}
