package com.mining.profit.pool.account;

/**
 * Interface for retrieving pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface PoolAccountExecutor {

    /**
     * Returns ETH account by wallet address.
     *
     * @param walletAddress the wallet address
     * @return ETH account
     * @throws PoolAccountExecutorException if there is any error in request executing
     * @throws PoolAccountException if there is any error in account creating
     */
    PoolAccount getETHAccount(String walletAddress) throws PoolAccountExecutorException, PoolAccountException;

}