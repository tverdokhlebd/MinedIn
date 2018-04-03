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
    /** Reported hashrate in H/s. */
    private BigDecimal reportedHashrate;

    /**
     * Creates the instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     * @param reportedHashrate reported hashrate
     */
    public Account(String walletAddress, BigDecimal walletBalance, BigDecimal reportedHashrate) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance.stripTrailingZeros();
        this.reportedHashrate = reportedHashrate != null ? reportedHashrate.stripTrailingZeros() : reportedHashrate;
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
     * Gets the reported hashrate.
     *
     * @return the reported hashrate
     */
    public BigDecimal getReportedHashrate() {
        return reportedHashrate;
    }

    /**
     * Sets the reported hashrate.
     *
     * @param reportedHashrate the new reported hashrate
     */
    public void setReportedHashrate(BigDecimal reportedHashrate) {
        this.reportedHashrate = reportedHashrate.stripTrailingZeros();
    }

}
