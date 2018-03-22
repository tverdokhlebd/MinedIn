package com.mined.in.market;

/**
 * Interface for retrieving market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MarketExecutor {

    /**
     * Returns a market.
     *
     * @return market
     * @throws MarketExecutorException if there is any error in request executing
     */
    Market getMarket() throws MarketExecutorException;

}
