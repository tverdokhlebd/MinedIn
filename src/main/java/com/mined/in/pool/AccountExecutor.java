package com.mined.in.pool;

/**
 * Interface for retrieving pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountExecutor {

    /**
     * Returns ETH account by wallet address.
     *
     * @param walletAddress the wallet address
     * @return ETH account
     * @throws AccountExecutorException if there is any error in account creating
     */
    Account getETHAccount(String walletAddress) throws AccountExecutorException;

}