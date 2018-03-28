package com.mined.in.worker;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketExecutor;
import com.mined.in.pool.AccountExecutor;
import com.mined.in.reward.RewardExecutor;
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
     * @param coinType coin type
     * @param accountExecutor pool account executor
     * @param marketExecutor market executor
     * @param rewardExecutor estimated rewards executor
     * @return mined worker
     */
    public static MinedWorker getMinedWorker(CoinType coinType, AccountExecutor accountExecutor, MarketExecutor marketExecutor,
            RewardExecutor rewardExecutor) {
        switch (coinType) {
        case ETH: {
            return new ETHMinedWorker(accountExecutor, marketExecutor, rewardExecutor);
        }
        default:
            throw new IllegalArgumentException(coinType.name());
        }
    }

}
