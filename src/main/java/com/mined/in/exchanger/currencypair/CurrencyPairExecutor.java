package com.mined.in.exchanger.currencypair;

/**
 * Interface for retrieving currency pair from exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface CurrencyPairExecutor {

    /**
     * Returns ETH/USD currency pair.
     *
     * @return ETH/USD currency pair
     * @throws CurrencyPairExecutorException if there is any error in request executing
     */
    CurrencyPair getETHUSDPair() throws CurrencyPairExecutorException;

}
