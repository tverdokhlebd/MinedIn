package com.mined.in.worker;

import com.mined.in.market.MarketExecutorException;
import com.mined.in.pool.AccountExecutorException;
import com.mined.in.reward.RewardExecutorException;

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
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     * @throws RewardExecutorException if there is any error in estimated reward creating
     */
    MinedEarnings calculate(String walletAddress) throws AccountExecutorException, MarketExecutorException, RewardExecutorException;

}
