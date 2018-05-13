package com.mined.in.earnings.worker;

import com.mined.in.description.CoinTypeDescription;
import com.mined.in.earnings.Earnings;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorException;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorException;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorException;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

/**
 * Interface of worker for calculating earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface EarningsWorker {

    /**
     * Calculates earnings.
     *
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @return calculated earnings
     * @throws AccountRequestorException if there is any error in account requesting
     * @throws CoinInfoRequestorException if there is any error in coin info requesting
     * @throws CoinMarketRequestorException if there is any error in coin market requesting
     * @throws CoinRewardRequestorException if there is any error in coin reward requesting
     */
    Earnings calculate(CoinTypeDescription coinType, String walletAddress)
            throws AccountRequestorException, CoinInfoRequestorException, CoinMarketRequestorException, CoinRewardRequestorException;

}
