package com.mined.in.exchanger.currencypair;

import java.math.BigDecimal;

/**
 * Class for representing currency pair of exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CurrencyPair {

    /** Pair name. */
    private final String pairName;
    /** Buy price. */
    private final BigDecimal buyPrice;
    /** Sell price. */
    private final BigDecimal sellPrice;

    /**
     * Creates the currency pair instance.
     *
     * @param pairName pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    public CurrencyPair(String pairName, BigDecimal buyPrice, BigDecimal sellPrice) {
        super();
        this.pairName = pairName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    /**
     * Gets the pair.
     *
     * @return the pair
     */
    public String getPairName() {
        return pairName;
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
