package com.mined.in.market;

import com.mined.in.coin.CoinMarket;

/**
 * Interface for requesting coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MarketRequestor {

    /**
     * Requests ethereum coin market.
     *
     * @return ethereum coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestEthereumCoin() throws MarketRequestorException;

    /**
     * Requests ethereum classic coin market.
     *
     * @return ethereum classic coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestEthereumClassicCoin() throws MarketRequestorException;

    /**
     * Requests zcash coin market.
     *
     * @return zcash coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestZcashCoin() throws MarketRequestorException;

}
