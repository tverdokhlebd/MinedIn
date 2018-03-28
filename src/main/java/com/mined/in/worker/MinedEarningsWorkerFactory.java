package com.mined.in.worker;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketExecutor;
import com.mined.in.pool.AccountExecutor;
import com.mined.in.reward.RewardExecutor;
import com.mined.in.worker.eth.ETHMinedEarningsWorker;

/**
 * Factory for creating mined earnings worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedEarningsWorkerFactory {

    /**
     * Creates mined earnings worker.
     *
     * @param coinType coin type
     * @param accountExecutor pool account executor
     * @param marketExecutor market executor
     * @param rewardExecutor estimated reward executor
     * @return mined earnings worker
     */
    public static MinedEarningsWorker create(CoinType coinType, AccountExecutor accountExecutor, MarketExecutor marketExecutor,
            RewardExecutor rewardExecutor) {
        switch (coinType) {
        case ETH: {
            return new ETHMinedEarningsWorker(accountExecutor, marketExecutor, rewardExecutor);
        }
        default:
            throw new IllegalArgumentException(coinType.name());
        }
    }

}
