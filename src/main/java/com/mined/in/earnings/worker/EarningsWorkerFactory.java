package com.mined.in.earnings.worker;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorFactory;
import com.mined.in.market.MarketType;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorFactory;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorFactory;
import com.mined.in.reward.RewardType;

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
     * @param poolType pool type
     * @param marketType market type
     * @param rewardType estimated reward type
     * @return earnings worker
     */
    public static EarningsWorker create(CoinType coinType, PoolType poolType, MarketType marketType, RewardType rewardType) {
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(marketType);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(rewardType);
        return create(coinType, accountRequestor, marketRequestor, rewardRequestor);
    }

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
        case ETC: {
            return new EthereumClassicWorker(accountRequestor, marketRequestor, rewardRequestor);
        }
        case XMR: {
            return new MoneroWorker(accountRequestor, marketRequestor, rewardRequestor);
        }
        case ZEC: {
            return new ZcashWorker(accountRequestor, marketRequestor, rewardRequestor);
        }
        default:
            throw new IllegalArgumentException(coinType.name());
        }
    }

}
