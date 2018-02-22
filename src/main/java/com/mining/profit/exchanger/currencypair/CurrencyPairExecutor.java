package com.mining.profit.exchanger.currencypair;

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
     * @throws CurrencyPairException if there is any error in currency pair creating
     */
    CurrencyPair getETHUSDPair() throws CurrencyPairExecutorException, CurrencyPairException;

}
