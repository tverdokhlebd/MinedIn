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
    /** Bitcoin coin market lock. */
    private static final Lock BITCOIN_LOCK = new ReentrantLock();
    /** Ethereum coin market lock. */
    private static final Lock ETHEREUM_LOCK = new ReentrantLock();
    /** Ethereum classic coin market lock. */
    private static final Lock ETHEREUM_CLASSIC_LOCK = new ReentrantLock();
    /** Monero coin market lock. */
    private static final Lock MONERO_LOCK = new ReentrantLock();
    /** Zcash coin market lock. */
    private static final Lock ZCASH_LOCK = new ReentrantLock();
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
    public CoinMarket requestBitcoinCoin() throws MarketRequestorException {
        BITCOIN_LOCK.lock();
        try {
            return new BitcoinRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            BITCOIN_LOCK.unlock();
        }
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

    @Override
    public CoinMarket requestEthereumClassicCoin() throws MarketRequestorException {
        ETHEREUM_CLASSIC_LOCK.lock();
        try {
            return new EthereumClassicRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            ETHEREUM_CLASSIC_LOCK.unlock();
        }
    }

    @Override
    public CoinMarket requestMoneroCoin() throws MarketRequestorException {
        MONERO_LOCK.lock();
        try {
            return new MoneroRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            MONERO_LOCK.unlock();
        }
    }

    @Override
    public CoinMarket requestZcashCoin() throws MarketRequestorException {
        ZCASH_LOCK.lock();
        try {
            return new ZcashRequestor(httpClient, ENDPOINTS_UPDATE).request();
        } finally {
            ZCASH_LOCK.unlock();
        }
    }

}
