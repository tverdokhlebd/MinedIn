package com.mined.in.worker;

import java.math.BigDecimal;

/**
 * Class for representing result of mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedResult {

    /** Balance of coins. */
    private final BigDecimal coinsBalance;
    /** Balance in USD. */
    private final BigDecimal usdBalance;
    /** Rate of USD. */
    private final BigDecimal usdRate;

    /**
     * Creates the mined result instance.
     *
     * @param coinsBalance balance of coins
     * @param usdBalance balance in USD
     * @param usdRate rate of USD
     */
    public MinedResult(BigDecimal coinsBalance, BigDecimal usdBalance, BigDecimal usdRate) {
        super();
        this.coinsBalance = coinsBalance;
        this.usdBalance = usdBalance;
        this.usdRate = usdRate;
    }

    /**
     * Gets the coins balance.
     *
     * @return the coins balance
     */
    public BigDecimal getCoinsBalance() {
        return coinsBalance;
    }

    /**
     * Gets the USD balance.
     *
     * @return the USD balance
     */
    public BigDecimal getUsdBalance() {
        return usdBalance;
    }

    /**
     * Gets the USD rate.
     *
     * @return the USD rate
     */
    public BigDecimal getUsdRate() {
        return usdRate;
    }

}
