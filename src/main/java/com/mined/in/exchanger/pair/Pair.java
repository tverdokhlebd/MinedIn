package com.mined.in.exchanger.pair;

import java.math.BigDecimal;

/**
 * Class for representing pair of exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Pair {

    /** Pair name. */
    private final PairName pairName;
    /** Buy price. */
    private final BigDecimal buyPrice;
    /** Sell price. */
    private final BigDecimal sellPrice;

    /**
     * Creates the pair instance.
     *
     * @param pairName pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    public Pair(PairName pairName, BigDecimal buyPrice, BigDecimal sellPrice) {
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
    public PairName getPairName() {
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
