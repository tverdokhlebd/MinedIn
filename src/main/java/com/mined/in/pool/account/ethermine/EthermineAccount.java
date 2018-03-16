package com.mined.in.pool.account.ethermine;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.pool.account.Account;

/**
 * Class for representing Ethermine account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthermineAccount extends Account {

    /** Wei. */
    private final static BigDecimal WEI = new BigDecimal("1000000000000000000");

    /**
     * Creates the Ethermine account instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     */
    private EthermineAccount(String walletAddress, BigDecimal walletBalance) {
        super(walletAddress, walletBalance);
    }

    /**
     * Creates the Ethermine account instance from JSON format.
     *
     * @param walletAddress wallet address
     * @param jsonAccount account in JSON format
     * @return Ethermine account instance
     */
    public static EthermineAccount create(String walletAddress, JSONObject jsonAccount) {
        BigDecimal walletBalance = new BigDecimal(0);
        JSONObject data = jsonAccount.optJSONObject("data");
        if (data != null) {
            Long balanceLong = data.getLong("unpaid");
            walletBalance = BigDecimal.valueOf(balanceLong);
            walletBalance = walletBalance.divide(WEI);
            walletBalance = walletBalance.stripTrailingZeros();
        }
        return new EthermineAccount(walletAddress, walletBalance);
    }

}
