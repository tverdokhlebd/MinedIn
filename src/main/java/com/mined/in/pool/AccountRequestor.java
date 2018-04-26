package com.mined.in.pool;

/**
 * Interface for requesting pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountRequestor {

    /**
     * Requests ethereum pool account.
     *
     * @param walletAddress the wallet address
     * @return ethereum pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    Account requestEthereumAccount(String walletAddress) throws AccountRequestorException;

    /**
     * Requests ethereum classic pool account.
     *
     * @param walletAddress the wallet address
     * @return ethereum classic pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    Account requestEthereumClassicAccount(String walletAddress) throws AccountRequestorException;

    /**
     * Requests monero pool account.
     *
     * @param walletAddress the wallet address
     * @return monero pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    Account requestMoneroAccount(String walletAddress) throws AccountRequestorException;

    /**
     * Requests zcash pool account.
     *
     * @param walletAddress the wallet address
     * @return zcash pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    Account requestZcashAccount(String walletAddress) throws AccountRequestorException;

}