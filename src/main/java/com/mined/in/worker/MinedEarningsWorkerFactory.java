package com.mined.in.worker;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketRequestor;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.reward.RewardRequestor;
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
     * @param accountRequestor pool account requestor
     * @param marketRequestor market requestor
     * @param rewardRequestor estimated reward requestor
     * @return mined earnings worker
     */
    public static MinedEarningsWorker create(CoinType coinType, AccountRequestor accountRequestor, MarketRequestor marketRequestor,
            RewardRequestor rewardRequestor) {
        switch (coinType) {
        case ETH: {
            return new ETHMinedEarningsWorker(accountRequestor, marketRequestor, rewardRequestor);
        }
        default:
            throw new IllegalArgumentException(coinType.name());
        }
    }

}
