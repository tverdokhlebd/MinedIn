package com.mined.in.pool;

/**
 * Interface for retrieving pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountRequestor {

    /**
     * Gets ETH account by wallet address.
     *
     * @param walletAddress the wallet address
     * @return ETH account
     * @throws AccountRequestorException if there is any error in account creating
     */
    Account getETHAccount(String walletAddress) throws AccountRequestorException;

}