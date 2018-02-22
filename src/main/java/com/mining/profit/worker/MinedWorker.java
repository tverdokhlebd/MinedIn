package com.mining.profit.worker;

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
     * @throws MinedWorkerException
     */
    MinedResult calculate(String walletAddress) throws MinedWorkerException;

}
