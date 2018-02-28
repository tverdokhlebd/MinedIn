package com.mined.in.worker;

import com.mined.in.coin.Coin;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutor;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.worker.eth.ETHMinedWorker;

/**
 * Factory for creating of mined worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedWorkerFactory {

    /**
     * Returns mined worker
     *
     * @param coin coin name
     * @param accountExecutor pool account executor
     * @param currencyPairExecutor exchanger currency pair executor
     * @return mined worker
     */
    public static MinedWorker getAccountExecutor(Coin coin, AccountExecutor accountExecutor, CurrencyPairExecutor currencyPairExecutor) {
        switch (coin) {
        case ETH: {
            return new ETHMinedWorker(accountExecutor, currencyPairExecutor);
        }
        default:
            throw new IllegalArgumentException(coin.name());
        }
    }

}
