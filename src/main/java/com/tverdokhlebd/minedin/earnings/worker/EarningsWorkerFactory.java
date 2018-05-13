package com.tverdokhlebd.minedin.earnings.worker;

import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestor;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorFactory;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestor;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorFactory;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestor;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorFactory;
import com.tverdokhlebd.minedin.description.CoinInfoDescription;
import com.tverdokhlebd.minedin.description.CoinMarketDescription;
import com.tverdokhlebd.minedin.description.CoinRewardDescription;
import com.tverdokhlebd.minedin.description.PoolTypeDescription;
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
     * @param poolType pool type
     * @param coinInfo coin info
     * @param coinMarket coin market
     * @param coinReward coin reward
     * @return earnings worker
     */
    public static EarningsWorker create(PoolTypeDescription poolType, CoinInfoDescription coinInfo, CoinMarketDescription coinMarket,
            CoinRewardDescription coinReward) {
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType.getPoolType());
        CoinInfoRequestor coinInfoRequestor = CoinInfoRequestorFactory.create(coinInfo.getCoinInfoType());
        CoinMarketRequestor coinMarketRequestor = CoinMarketRequestorFactory.create(coinMarket.getCoinMarketType());
        CoinRewardRequestor coinRewardRequestor = CoinRewardRequestorFactory.create(coinReward.getCoinRewardType());
        return new MinedInWorker(accountRequestor, coinInfoRequestor, coinMarketRequestor, coinRewardRequestor);
    }

}
