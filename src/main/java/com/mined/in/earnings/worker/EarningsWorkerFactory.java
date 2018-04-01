package com.mined.in.earnings.worker;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketRequestor;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.reward.RewardRequestor;

/**
 * Factory for creating earnings worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EarningsWorkerFactory {

    /**
     * Creates earnings worker.
     *
     * @param coinType coin type
     * @param accountRequestor pool account requestor
     * @param marketRequestor market requestor
     * @param rewardRequestor estimated reward requestor
     * @return earnings worker
     */
    public static EarningsWorker create(CoinType coinType, AccountRequestor accountRequestor, MarketRequestor marketRequestor,
            RewardRequestor rewardRequestor) {
        switch (coinType) {
        case ETH: {
            return new EthereumWorker(accountRequestor, marketRequestor, rewardRequestor);
        }
        default:
            throw new IllegalArgumentException(coinType.name());
        }
    }

}
