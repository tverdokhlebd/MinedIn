package com.mining.profit.worker;

import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.pool.account.AccountExecutor;

/**
 * Class for calculating of mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedWorker {

    /** Pool account executor. */
    private final AccountExecutor accountExecutor;
    /** Exchanger currency pair executor. */
    private final CurrencyPairExecutor currencyPairExecutor;

    /**
     * Creates the mined calculation instance.
     *
     * @param accountExecutor pool account executor
     * @param currencyPairExecutor exchanger currency pair executor
     */
    public MinedWorker(AccountExecutor accountExecutor, CurrencyPairExecutor currencyPairExecutor) {
        super();
        this.accountExecutor = accountExecutor;
        this.currencyPairExecutor = currencyPairExecutor;
    }

    /**
     * Calculates mined and returns mined result.
     *
     * @param walletAddress the wallet address
     * @return mined result
     */
    public MinedResult calculateMined(String walletAddress) {
        return null;
    }

}
