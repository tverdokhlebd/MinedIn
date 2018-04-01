package com.mined.in.earnings.worker;

import com.mined.in.earnings.Earnings;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.reward.RewardRequestorException;

/**
 * Interface of worker for calculating earnings of pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface EarningsWorker {

    /**
     * Calculates earnings of pool account.
     *
     * @param walletAddress wallet address
     * @return earnings
     * @throws AccountRequestorException if there is any error in account requesting
     * @throws MarketRequestorException if there is any error in market requesting
     * @throws RewardRequestorException if there is any error in estimated reward requesting
     */
    Earnings calculate(String walletAddress) throws AccountRequestorException, MarketRequestorException, RewardRequestorException;

}
