package com.mined.in.pool.account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Class for representing pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Account {

    /** Wallet address. */
    private final String walletAddress;
    /** Wallet balance. */
    private final BigDecimal walletBalance;
    /** Reported total hashrate. */
    private double totalHashrate;
    /** List of workers. */
    private List<Worker> workerList;

    /**
     * Creates the pool account instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     * @param totalHashrate reported total hashrate
     * @param workerList list of workers
     */
    public Account(String walletAddress, BigDecimal walletBalance, double totalHashrate, List<Worker> workerList) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance;
        this.totalHashrate = totalHashrate;
        this.workerList = workerList;
    }

    /**
     * Gets the wallet address.
     *
     * @return the wallet address
     */
    public String getWalletAddress() {
        return walletAddress;
    }

    /**
     * Gets the wallet balance.
     *
     * @return the wallet balance
     */
    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    /**
     * Gets the total hashrate.
     *
     * @return the total hashrate
     */
    public double getTotalHashrate() {
        return totalHashrate;
    }

    /**
     * Sets the total hashrate.
     *
     * @param totalHashrate the new total hashrate
     */
    public void setTotalHashrate(double totalHashrate) {
        this.totalHashrate = totalHashrate;
    }

    /**
     * Gets the worker list.
     *
     * @return the worker list
     */
    public List<Worker> getWorkerList() {
        return workerList;
    }

    /**
     * Sets the worker list.
     *
     * @param workerList the new worker list
     */
    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }

}
