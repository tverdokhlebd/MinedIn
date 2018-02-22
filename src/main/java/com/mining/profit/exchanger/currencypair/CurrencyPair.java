package com.mining.profit.exchanger.currencypair;

import java.math.BigDecimal;

/**
 * Class for representing currency pair of exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CurrencyPair {

    /** Pair name. */
    private final String pair;
    /** Buy price. */
    private final BigDecimal buyPrice;
    /** Sell price. */
    private final BigDecimal sellPrice;

    /**
     * Creates the currency pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    public CurrencyPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super();
        this.pair = pair;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    /**
     * Gets the pair.
     *
     * @return the pair
     */
    public String getPair() {
        return pair;
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
