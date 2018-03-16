package com.mined.in.worker;

import com.mined.in.coin.Coin;
import com.mined.in.exchanger.pair.PairExecutor;
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
     * Returns mined worker.
     *
     * @param coin coin name
     * @param accountExecutor pool account executor
     * @param pairExecutor exchanger pair executor
     * @return mined worker
     */
    public static MinedWorker getMinedWorker(Coin coin, AccountExecutor accountExecutor, PairExecutor pairExecutor) {
        switch (coin) {
        case ETH: {
            return new ETHMinedWorker(accountExecutor, pairExecutor);
        }
        default:
            throw new IllegalArgumentException(coin.name());
        }
    }

}
