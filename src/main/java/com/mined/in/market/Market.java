package com.mined.in.market;

import java.math.BigDecimal;

/**
 * Class for representing a market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Market {

    /** Ethereum price. */
    private final BigDecimal ethPrice;

    /**
     * Creates the instance.
     *
     * @param ethPrice ethereum price
     */
    public Market(BigDecimal ethPrice) {
        super();
        this.ethPrice = ethPrice;
    }

    /**
     * Gets the ethereum price.
     *
     * @return the ethereum price
     */
    public BigDecimal getEthPrice() {
        return ethPrice;
    }

    /**
     * Class for representing the market builder.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static class MarketBuilder {

        /** Ethereum price. */
        private BigDecimal ethPrice;

        /**
         * Creates the instance.
         */
        public MarketBuilder() {
            super();
        }

        /**
         * Sets ethereum price.
         *
         * @param ethPrice the ethereum price
         * @return the market builder
         */
        public MarketBuilder ethPrice(BigDecimal ethPrice) {
            this.ethPrice = ethPrice.stripTrailingZeros();
            return this;
        }

        /**
         * Returns created market.
         *
         * @return created market
         */
        public Market createMarket() {
            return new Market(ethPrice);
        }

    }

}
