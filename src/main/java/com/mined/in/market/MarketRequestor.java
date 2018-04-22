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
     * Requests bitcoin coin market.
     *
     * @return bitcoin coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestBitcoinCoin() throws MarketRequestorException;

    /**
     * Requests ethereum coin market.
     *
     * @return ethereum coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestEthereumCoin() throws MarketRequestorException;

    /**
     * Requests monero coin market.
     *
     * @return monero coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    CoinMarket requestMoneroCoin() throws MarketRequestorException;

}
