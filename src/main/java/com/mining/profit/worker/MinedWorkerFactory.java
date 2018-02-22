package com.mining.profit.worker;

import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.pool.account.AccountExecutor;
import com.mining.profit.worker.eth.ETHMinedWorker;

/**
 * Factory for creating of mined worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedWorkerFactory {

    /**
     * Enumeration of mined worker type.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum MinedWorkerType {
        ETH
    }

    /**
     * Returns mined worker
     *
     * @param type type type of mined worker
     * @param accountExecutor pool account executor
     * @param currencyPairExecutor exchanger currency pair executor
     * @return mined worker
     */
    public static MinedWorker getAccountExecutor(MinedWorkerType type, AccountExecutor accountExecutor,
            CurrencyPairExecutor currencyPairExecutor) {
        switch (type) {
        case ETH: {
            return new ETHMinedWorker(accountExecutor, currencyPairExecutor);
        }
        default:
            throw new IllegalArgumentException(type.name());
        }
    }

}
