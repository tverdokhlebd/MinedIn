package com.mined.in.pool;

import java.math.BigDecimal;

/**
 * Pool account. It uses as intermediate result of calculations.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Account {

    /** Wallet address. */
    private final String walletAddress;
    /** Wallet balance. */
    private final BigDecimal walletBalance;
    /** Reported total hashrate in H/s. */
    private BigDecimal totalHashrate;

    /**
     * Creates the instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     * @param totalHashrate reported total hashrate
     */
    public Account(String walletAddress, BigDecimal walletBalance, BigDecimal totalHashrate) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance.stripTrailingZeros();
        this.totalHashrate = totalHashrate != null ? totalHashrate.stripTrailingZeros() : totalHashrate;
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
    public BigDecimal getTotalHashrate() {
        return totalHashrate;
    }

    /**
     * Sets the total hashrate.
     *
     * @param totalHashrate the new total hashrate
     */
    public void setTotalHashrate(BigDecimal totalHashrate) {
        this.totalHashrate = totalHashrate.stripTrailingZeros();
    }

}
