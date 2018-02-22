package com.mining.profit.worker;

import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.pool.account.AccountExecutor;

/**
 * Class for calculating of profit.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ProfitWorker {

    /** Pool account executor. */
    private final AccountExecutor poolAccountExecutor;
    /** Exchanger currency pair executor. */
    private final CurrencyPairExecutor exchangerCurrencyPairExecutor;

    /**
     * Creates the profit calculation instance.
     *
     * @param poolAccountExecutor pool account executor
     * @param exchangerCurrencyPairExecutor exchanger currency pair executor
     */
    public ProfitWorker(AccountExecutor poolAccountExecutor, CurrencyPairExecutor exchangerCurrencyPairExecutor) {
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
