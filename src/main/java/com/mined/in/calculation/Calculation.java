package com.mined.in.calculation;

import java.math.BigDecimal;

import com.mined.in.coin.CoinType;

/**
 * Class for representing a mining calculation.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Calculation {

    /** Type of coin. */
    private final CoinType coinType;
    /** Reported total hashrate. */
    private final BigDecimal totalHashrate;
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
    /** Reward per hour. */
    private final BigDecimal rewardPerHour;
    /** Reward per day. */
    private final BigDecimal rewardPerDay;
    /** Reward per week. */
    private final BigDecimal rewardPerWeek;
    /** Reward per month. */
    private final BigDecimal rewardPerMonth;
    /** Reward per year. */
    private final BigDecimal rewardPerYear;

    /**
     * Creates the instance.
     *
     * @param coinType type of coin
     * @param totalHashrate reported total hashrate
     * @param blockTime block time in seconds
     * @param blockReward block reward
     * @param blockCount block count
     * @param difficulty difficulty
     * @param networkHashrate network hashrate
     * @param rewardPerHour reward per hour
     * @param rewardPerDay reward per day
     * @param rewardPerWeek reward per week
     * @param rewardPerMonth reward per month
     * @param rewardPerYear reward per year
     */
    public Calculation(CoinType coinType, BigDecimal totalHashrate, BigDecimal blockTime, BigDecimal blockReward, BigDecimal blockCount,
            BigDecimal difficulty, BigDecimal networkHashrate, BigDecimal rewardPerHour, BigDecimal rewardPerDay, BigDecimal rewardPerWeek,
            BigDecimal rewardPerMonth, BigDecimal rewardPerYear) {
        super();
        this.coinType = coinType;
        this.totalHashrate = totalHashrate;
        this.blockTime = blockTime;
        this.blockReward = blockReward;
        this.blockCount = blockCount;
        this.difficulty = difficulty;
        this.networkHashrate = networkHashrate;
        this.rewardPerHour = rewardPerHour;
        this.rewardPerDay = rewardPerDay;
        this.rewardPerWeek = rewardPerWeek;
        this.rewardPerMonth = rewardPerMonth;
        this.rewardPerYear = rewardPerYear;
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
     * Gets the total hashrate.
     *
     * @return the total hashrate
     */
    public BigDecimal getTotalHashrate() {
        return totalHashrate;
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
     * Gets the reward per hour.
     *
     * @return the reward per hour
     */
    public BigDecimal getRewardPerHour() {
        return rewardPerHour;
    }

    /**
     * Gets the reward per day.
     *
     * @return the reward per day
     */
    public BigDecimal getRewardPerDay() {
        return rewardPerDay;
    }

    /**
     * Gets the reward per week.
     *
     * @return the reward per week
     */
    public BigDecimal getRewardPerWeek() {
        return rewardPerWeek;
    }

    /**
     * Gets the reward per month.
     *
     * @return the reward per month
     */
    public BigDecimal getRewardPerMonth() {
        return rewardPerMonth;
    }

    /**
     * Gets the reward per year.
     *
     * @return the reward per year
     */
    public BigDecimal getRewardPerYear() {
        return rewardPerYear;
    }

    /**
     * Class for representing the calculation builder.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static class CalculationBuilder {

        /** Type of coin. */
        private CoinType coinType;
        /** Reported total hashrate. */
        private BigDecimal totalHashrate;
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
        /** Reward per hour. */
        private BigDecimal rewardPerHour;
        /** Reward per day. */
        private BigDecimal rewardPerDay;
        /** Reward per week. */
        private BigDecimal rewardPerWeek;
        /** Reward per month. */
        private BigDecimal rewardPerMonth;
        /** Reward per year. */
        private BigDecimal rewardPerYear;

        /**
         * Creates the instance.
         */
        public CalculationBuilder() {
            super();
        }

        /**
         * Sets the coin type.
         *
         * @param coinType the new coin type
         */
        public CalculationBuilder coinType(CoinType coinType) {
            this.coinType = coinType;
            return this;
        }

        /**
         * Sets the total hashrate.
         *
         * @param totalHashrate the new total hashrate
         */
        public CalculationBuilder totalHashrate(BigDecimal totalHashrate) {
            this.totalHashrate = totalHashrate.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the block time.
         *
         * @param blockTime the new block time
         */
        public CalculationBuilder blockTime(BigDecimal blockTime) {
            this.blockTime = blockTime.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the block reward.
         *
         * @param blockReward the new block reward
         */
        public CalculationBuilder blockReward(BigDecimal blockReward) {
            this.blockReward = blockReward.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the block count.
         *
         * @param blockCount the new block count
         */
        public CalculationBuilder blockCount(BigDecimal blockCount) {
            this.blockCount = blockCount.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the difficulty.
         *
         * @param difficulty the new difficulty
         */
        public CalculationBuilder difficulty(BigDecimal difficulty) {
            this.difficulty = difficulty.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the network hashrate.
         *
         * @param networkHashrate the new network hashrate
         */
        public CalculationBuilder networkHashrate(BigDecimal networkHashrate) {
            this.networkHashrate = networkHashrate.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per hour.
         *
         * @param rewardPerHour the new reward per hour
         */
        public CalculationBuilder rewardPerHour(BigDecimal rewardPerHour) {
            this.rewardPerHour = rewardPerHour.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per day.
         *
         * @param rewardPerDay the new reward per day
         */
        public CalculationBuilder rewardPerDay(BigDecimal rewardPerDay) {
            this.rewardPerDay = rewardPerDay.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per week.
         *
         * @param rewardPerWeek the new reward per week
         */
        public CalculationBuilder rewardPerWeek(BigDecimal rewardPerWeek) {
            this.rewardPerWeek = rewardPerWeek.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per month.
         *
         * @param rewardPerMonth the new reward per month
         */
        public CalculationBuilder rewardPerMonth(BigDecimal rewardPerMonth) {
            this.rewardPerMonth = rewardPerMonth.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per year.
         *
         * @param rewardPerYear the new reward per year
         */
        public CalculationBuilder rewardPerYear(BigDecimal rewardPerYear) {
            this.rewardPerYear = rewardPerYear.stripTrailingZeros();
            return this;
        }

        /**
         * Returns created calculation.
         *
         * @return created calculation
         */
        public Calculation createCalculation() {
            return new Calculation(coinType,
                                   totalHashrate,
                                   blockTime,
                                   blockReward,
                                   blockCount,
                                   difficulty,
                                   networkHashrate,
                                   rewardPerHour,
                                   rewardPerDay,
                                   rewardPerWeek,
                                   rewardPerMonth,
                                   rewardPerYear);
        }

    }

}
