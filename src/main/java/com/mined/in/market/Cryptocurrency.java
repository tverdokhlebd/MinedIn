package com.mined.in.market;

import java.math.BigDecimal;

/**
 * Class for representing market.
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
    public static class Builder {

        /** Ethereum price. */
        private BigDecimal ethPrice;

        /**
         * Creates the instance.
         */
        public Builder() {
            super();
        }

        /**
         * Sets ethereum price.
         *
         * @param ethPrice the ethereum price
         * @return the market builder
         */
        public Builder ethPrice(BigDecimal ethPrice) {
            this.ethPrice = ethPrice.stripTrailingZeros();
            return this;
        }

        /**
         * Build market.
         *
         * @return market
         */
        public Market build() {
            return new Market(ethPrice);
        }

    }

}
