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
    /** Buy price. */
    private final BigDecimal buyPrice;
    /** Sell price. */
    private final BigDecimal sellPrice;

    /**
     * Creates the mined result instance.
     *
     * @param coinsBalance balance of coins
     * @param usdBalance balance in USD
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    public MinedResult(BigDecimal coinsBalance, BigDecimal usdBalance, BigDecimal buyPrice, BigDecimal sellPrice) {
        super();
        this.coinsBalance = coinsBalance;
        this.usdBalance = usdBalance;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
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
     * Gets the buy price.
     *
     * @return the buy price
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     * Gets the sell price.
     *
     * @return the sell price
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

}
