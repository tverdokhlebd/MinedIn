package com.mined.in.worker;

import java.math.BigDecimal;

import com.mined.in.calculator.Calculation;

/**
 * Class for representing result of mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MinedResult {

    /** Balance of coins. */
    private final BigDecimal coinBalance;
    /** Balance in USD. */
    private final BigDecimal usdBalance;
    /** Coin price. */
    private final BigDecimal coinPrice;
    /** Mining calculation. */
    private final Calculation calculation;

    /**
     * Creates the mined result instance.
     *
     * @param coinBalance balance of coins
     * @param usdBalance balance in USD
     * @param coinPrice buy price
     * @param calculation mining calculation
     */
    public MinedResult(BigDecimal coinBalance, BigDecimal usdBalance, BigDecimal coinPrice, Calculation calculation) {
        super();
        this.coinBalance = coinBalance;
        this.usdBalance = usdBalance;
        this.coinPrice = coinPrice;
        this.calculation = calculation;
    }

    /**
     * Gets the coins balance.
     *
     * @return the coins balance
     */
    public BigDecimal getCoinBalance() {
        return coinBalance;
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
     * Gets the coin price.
     *
     * @return the coin price
     */
    public BigDecimal getCoinPrice() {
        return coinPrice;
    }

    /**
     * Gets the calculation.
     *
     * @return the calculation
     */
    public Calculation getCalculation() {
        return calculation;
    }

}
