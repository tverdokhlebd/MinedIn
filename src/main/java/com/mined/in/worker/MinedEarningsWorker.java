package com.mined.in.worker;

import com.mined.in.market.MarketRequestorException;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.reward.RewardRequestorException;

/**
 * Interface of worker for calculating mined earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MinedEarningsWorker {

    /**
     * Calculates mined earnings.
     *
     * @param walletAddress the wallet address
     * @return mined earnings
     * @throws AccountRequestorException if there is any error in account creating
     * @throws MarketRequestorException if there is any error in market creating
     * @throws RewardRequestorException if there is any error in estimated reward creating
     */
    MinedEarnings calculate(String walletAddress) throws AccountRequestorException, MarketRequestorException, RewardRequestorException;

}
