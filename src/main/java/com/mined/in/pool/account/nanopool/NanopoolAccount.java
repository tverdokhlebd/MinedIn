package com.mined.in.pool.account.nanopool;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.pool.account.Account;

/**
 * Class for representing Nanopool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class NanopoolAccount extends Account {

    /**
     * Creates the Nanopool account instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     */
    private NanopoolAccount(String walletAddress, BigDecimal walletBalance) {
        super(walletAddress, walletBalance);
    }

    /**
     * Creates the Nanopool account instance from JSON format.
     *
     * @param walletAddress wallet address
     * @param jsonAccount account in JSON format
     * @return Ethermine account instance
     */
    public static NanopoolAccount create(String walletAddress, JSONObject jsonAccount) {
        BigDecimal walletBalance = BigDecimal.valueOf(jsonAccount.getDouble("data"));
        walletBalance = walletBalance.stripTrailingZeros();
        return new NanopoolAccount(walletAddress, walletBalance);
    }

}
