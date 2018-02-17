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
     */
    PoolAccount getETHAccount(String walletAddress);

}