package com.mining.profit.exchanger.currencypair;

/**
 * Interface for retrieving currency pair from exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface ExchangerCurrencyPairExecutor {

    /**
     * Returns ETH/USD currency pair.
     *
     * @return ETH/USD currency pair
     */
    ExchangerCurrencyPair getETHUSDPair();

    /**
     * Returns ETH/UAH currency pair.
     *
     * @return ETH/UAH currency pair
     */
    ExchangerCurrencyPair getETHUAHPair();

}
