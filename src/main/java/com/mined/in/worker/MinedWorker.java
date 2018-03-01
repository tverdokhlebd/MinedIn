package com.mined.in.worker;

import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.pool.account.AccountExecutorException;

/**
 * Interface for calculating of mined.
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
     * @throws PairExecutorException if there is any error in pair creating
     */
    MinedResult calculate(String walletAddress) throws AccountExecutorException, PairExecutorException;

}
