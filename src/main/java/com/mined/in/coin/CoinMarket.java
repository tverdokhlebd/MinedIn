package com.mined.in.coin;

import java.math.BigDecimal;

/**
 * Market information about coin (rank, price, market cap and etc.).
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarket {

    /** Type of coin. */
    private final CoinType coin;
    /** Price of coin. */
    private final BigDecimal price;

    /**
     * Creates the instance.
     *
     * @param coin type of coin
     * @param price price of coin
     */
    public CoinMarket(CoinType coin, BigDecimal price) {
        super();
        this.coin = coin;
        this.price = price.stripTrailingZeros();
    }

    /**
     * Gets the coin.
     *
     * @return the coin
     */
    public CoinType getCoin() {
        return coin;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

}
