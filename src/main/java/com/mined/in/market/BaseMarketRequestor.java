package com.mined.in.market;

import java.util.Date;

import com.mined.in.coin.CoinMarket;
import com.mined.in.http.BaseRequestor;

/**
 * Base market requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of the argument
 * @param <R> type of the result
 */
public interface BaseMarketRequestor<T, R> extends BaseRequestor<T, R> {

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
