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

    /**
     * Creates the pool account instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     */
    public Account(String walletAddress, BigDecimal walletBalance) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance;
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

}
