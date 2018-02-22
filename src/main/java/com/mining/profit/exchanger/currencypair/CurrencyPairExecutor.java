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
     */
    CurrencyPair getETHUSDPair();

    /**
     * Returns ETH/UAH currency pair.
     *
     * @return ETH/UAH currency pair
     */
    CurrencyPair getETHUAHPair();

}
