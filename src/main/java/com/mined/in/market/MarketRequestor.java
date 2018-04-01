package com.mined.in.market;

import com.mined.in.coin.CoinMarket;

/**
 * Interface for retrieving coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MarketRequestor {

    /**
     * Gets ETH coin market.
     *
     * @return ETH coin market
     * @throws MarketRequestorException if there is any error in request executing
     */
    CoinMarket getETHCoin() throws MarketRequestorException;

}
