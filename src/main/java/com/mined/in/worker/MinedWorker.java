package com.mined.in.worker;

import com.mined.in.market.MarketExecutorException;
import com.mined.in.pool.account.AccountExecutorException;

/**
 * Interface of worker for calculating of mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface MinedWorker {

    /**
     * Calculates mined and returns mined result.
     *
     * @param walletAddress the wallet address
     * @return mined result
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     */
    MinedResult calculate(String walletAddress) throws AccountExecutorException, MarketExecutorException;

}
