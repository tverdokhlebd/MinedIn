package com.mining.profit.worker;

import java.math.BigDecimal;

/**
 * Class for representing result of profit.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ProfitResult {

    /** Balance of coins. */
    private final BigDecimal coinsBalance;
    /** Balance in USD. */
    private final BigDecimal usdBalance;
    /** Rate of USD. */
    private final BigDecimal usdRate;
    /** Balance in UAH. */
    private final BigDecimal uahBalance;
    /** Rate of UAH. */
    private final BigDecimal uahRate;

    /**
     * Creates the result profit instance.
     *
     * @param coinsBalance balance of coins
     * @param usdBalance balance in USD
     * @param usdRate rate of USD
     * @param uahBalance balance in UAH
     * @param uahRate rate of UAH
     */
    public ProfitResult(BigDecimal coinsBalance, BigDecimal usdBalance, BigDecimal usdRate, BigDecimal uahBalance, BigDecimal uahRate) {
        super();
        this.coinsBalance = coinsBalance;
        this.usdBalance = usdBalance;
        this.usdRate = usdRate;
        this.uahBalance = uahBalance;
        this.uahRate = uahRate;
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

    /**
     * Gets the UAH balance.
     *
     * @return the UAH balance
     */
    public BigDecimal getUahBalance() {
        return uahBalance;
    }

    /**
     * Gets the UAH rate.
     *
     * @return the UAH rate
     */
    public BigDecimal getUahRate() {
        return uahRate;
    }

}
