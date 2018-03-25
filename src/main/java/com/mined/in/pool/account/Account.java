package com.mined.in.pool.account;

import java.math.BigDecimal;

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

    /**
     * Creates the pool account instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     * @param totalHashrate reported total hashrate
     */
    public Account(String walletAddress, BigDecimal walletBalance, double totalHashrate) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance;
        this.totalHashrate = totalHashrate;
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

}
