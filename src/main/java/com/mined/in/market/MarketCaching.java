package com.mined.in.market;

import java.util.Date;

import com.mined.in.coin.CoinMarket;

/**
 * Interface for market caching.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MarketCaching {

    /**
     * Gets cached next update.
     *
     * @return cached next update
     */
    Date getCachedNextUpdate();

    /**
     * Sets cached next update.
     *
     * @param nextUpdate next update
     */
    void setCachedNextUpdate(Date nextUpdate);

    /**
     * Gets cached coin market.
     *
     * @return cached coin info
     */
    CoinMarket getCachedCoinMarket();

    /**
     * Sets cached coin market.
     *
     * @param coinMarket coin market
     */
    void setCachedCoinMarket(CoinMarket coinMarket);

}
