package com.mined.in.earnings.worker;

import com.mined.in.description.CoinInfoDescription;
import com.mined.in.description.CoinMarketDescription;
import com.mined.in.description.CoinRewardDescription;
import com.mined.in.description.PoolTypeDescription;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestor;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorFactory;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestor;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorFactory;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestor;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorFactory;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestor;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorFactory;

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
     * @param poolInfo pool info
     * @param coinInfo coin info
     * @param marketInfo market info
     * @param rewardInfo reward info
     * @return earnings worker
     */
    public static EarningsWorker create(PoolTypeDescription poolInfo, CoinInfoDescription coinInfo, CoinMarketDescription marketInfo,
            CoinRewardDescription rewardInfo) {
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolInfo.getPoolType());
        CoinInfoRequestor coinInfoRequestor = CoinInfoRequestorFactory.create(coinInfo.getCoinInfoType());
        CoinMarketRequestor coinMarketRequestor = CoinMarketRequestorFactory.create(marketInfo.getCoinMarketType());
        CoinRewardRequestor coinRewardRequestor = CoinRewardRequestorFactory.create(rewardInfo.getCoinRewardType());
        return new MinedInWorker(accountRequestor, coinInfoRequestor, coinMarketRequestor, coinRewardRequestor);
    }

}
