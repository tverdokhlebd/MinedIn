package com.mined.in.coin;

import java.math.BigDecimal;

/**
 * Class for representing a coin information.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinInfo {

    /** Type of coin. */
    private final CoinType coinType;
    /** Block time in seconds. */
    private final BigDecimal blockTime;
    /** Block reward. */
    private final BigDecimal blockReward;
    /** Block count. */
    private final BigDecimal blockCount;
    /** Difficulty. */
    private final BigDecimal difficulty;
    /** Network hashrate. */
    private final BigDecimal networkHashrate;

    /**
     * Creates the instance.
     *
     * @param coinType type of coin
     * @param blockTime block time in seconds
     * @param blockReward block reward
     * @param blockCount block count
     * @param difficulty difficulty
     * @param networkHashrate network hashrate
     */
    public CoinInfo(CoinType coinType, BigDecimal blockTime, BigDecimal blockReward, BigDecimal blockCount, BigDecimal difficulty,
            BigDecimal networkHashrate) {
        super();
        this.coinType = coinType;
        this.blockTime = blockTime;
        this.blockReward = blockReward;
        this.blockCount = blockCount;
        this.difficulty = difficulty;
        this.networkHashrate = networkHashrate;
    }

    /**
     * Gets the coin type.
     *
     * @return the coin type
     */
    public CoinType getCoinType() {
        return coinType;
    }

    /**
     * Gets the block time.
     *
     * @return the block time
     */
    public BigDecimal getBlockTime() {
        return blockTime;
    }

    /**
     * Gets the block reward.
     *
     * @return the block reward
     */
    public BigDecimal getBlockReward() {
        return blockReward;
    }

    /**
     * Gets the block count.
     *
     * @return the block count
     */
    public BigDecimal getBlockCount() {
        return blockCount;
    }

    /**
     * Gets the difficulty.
     *
     * @return the difficulty
     */
    public BigDecimal getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the network hashrate.
     *
     * @return the network hashrate
     */
    public BigDecimal getNetworkHashrate() {
        return networkHashrate;
    }

    /**
     * Class for representing a coin information builder.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static class CoinInfoBuilder {

        /** Type of coin. */
        private CoinType coinType;
        /** Block time in seconds. */
        private BigDecimal blockTime;
        /** Block reward. */
        private BigDecimal blockReward;
        /** Block count. */
        private BigDecimal blockCount;
        /** Difficulty. */
        private BigDecimal difficulty;
        /** Network hashrate. */
        private BigDecimal networkHashrate;

        /**
         * Creates the instance.
         */
        public CoinInfoBuilder() {
            super();
        }

        /**
         * Sets the coin type.
         *
         * @param coinType the new coin type
         */
        public CoinInfoBuilder coinType(CoinType coinType) {
            this.coinType = coinType;
            return this;
        }

        /**
         * Sets the block time.
         *
         * @param blockTime the new block time
         */
        public CoinInfoBuilder blockTime(BigDecimal blockTime) {
            this.blockTime = blockTime.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the block reward.
         *
         * @param blockReward the new block reward
         */
        public CoinInfoBuilder blockReward(BigDecimal blockReward) {
            this.blockReward = blockReward.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the block count.
         *
         * @param blockCount the new block count
         */
        public CoinInfoBuilder blockCount(BigDecimal blockCount) {
            this.blockCount = blockCount.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the difficulty.
         *
         * @param difficulty the new difficulty
         */
        public CoinInfoBuilder difficulty(BigDecimal difficulty) {
            this.difficulty = difficulty.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the network hashrate.
         *
         * @param networkHashrate the new network hashrate
         */
        public CoinInfoBuilder networkHashrate(BigDecimal networkHashrate) {
            this.networkHashrate = networkHashrate.stripTrailingZeros();
            return this;
        }

        /**
         * Returns created coin information.
         *
         * @return created coin information
         */
        public CoinInfo createCoinInfo() {
            return new CoinInfo(coinType, blockTime, blockReward, blockCount, difficulty, networkHashrate);
        }

    }

}
