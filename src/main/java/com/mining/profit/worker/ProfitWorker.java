package com.mining.profit.worker;

import com.mining.profit.exchanger.currencypair.ExchangerCurrencyPairExecutor;
import com.mining.profit.pool.account.PoolAccountExecutor;

/**
 * Class for calculating of profit.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ProfitWorker {

    /** Pool account executor. */
    private final PoolAccountExecutor poolAccountExecutor;
    /** Exchanger currency pair executor. */
    private final ExchangerCurrencyPairExecutor exchangerCurrencyPairExecutor;

    /**
     * Creates the profit calculation instance.
     *
     * @param poolAccountExecutor pool account executor
     * @param exchangerCurrencyPairExecutor exchanger currency pair executor
     */
    public ProfitWorker(PoolAccountExecutor poolAccountExecutor, ExchangerCurrencyPairExecutor exchangerCurrencyPairExecutor) {
        super();
        this.poolAccountExecutor = poolAccountExecutor;
        this.exchangerCurrencyPairExecutor = exchangerCurrencyPairExecutor;
    }

    /**
     * Calculates profit and returns profit result.
     *
     * @param walletAddress the wallet address
     * @return profit result
     */
    public ProfitResult calculateProfit(String walletAddress) {
        return null;
    }

}
