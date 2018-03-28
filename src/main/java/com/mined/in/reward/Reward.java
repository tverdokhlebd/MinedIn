package com.mined.in.reward;

import java.math.BigDecimal;

import com.mined.in.coin.CoinInfo;

/**
 * Class for representing estimated rewards.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Reward {

    /** Coin information. */
    private final CoinInfo coinInfo;
    /** Reported total hashrate. */
    private final BigDecimal totalHashrate;
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
     * @param coinInfo coin information
     * @param totalHashrate reported total hashrate
     * @param rewardPerHour reward per hour
     * @param rewardPerDay reward per day
     * @param rewardPerWeek reward per week
     * @param rewardPerMonth reward per month
     * @param rewardPerYear reward per year
     */
    public Reward(CoinInfo coinInfo, BigDecimal totalHashrate, BigDecimal rewardPerHour, BigDecimal rewardPerDay, BigDecimal rewardPerWeek,
            BigDecimal rewardPerMonth,
            BigDecimal rewardPerYear) {
        super();
        this.coinInfo = coinInfo;
        this.totalHashrate = totalHashrate;
        this.rewardPerHour = rewardPerHour;
        this.rewardPerDay = rewardPerDay;
        this.rewardPerWeek = rewardPerWeek;
        this.rewardPerMonth = rewardPerMonth;
        this.rewardPerYear = rewardPerYear;
    }

    /**
     * Gets the coin info.
     *
     * @return the coin info
     */
    public CoinInfo getCoinInfo() {
        return coinInfo;
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
     * Class for representing the estimated rewards builder.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static class RewardBuilder {

        /** Coin information. */
        private CoinInfo coinInfo;
        /** Reported total hashrate. */
        private BigDecimal totalHashrate;
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
        public RewardBuilder() {
            super();
        }

        /**
         * Sets the coin info.
         *
         * @param coinInfo the new coin info
         */
        public RewardBuilder coinInfo(CoinInfo coinInfo) {
            this.coinInfo = coinInfo;
            return this;
        }

        /**
         * Sets the total hashrate.
         *
         * @param totalHashrate the new total hashrate
         */
        public RewardBuilder setTotalHashrate(BigDecimal totalHashrate) {
            this.totalHashrate = totalHashrate.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per hour.
         *
         * @param rewardPerHour the new reward per hour
         */
        public RewardBuilder rewardPerHour(BigDecimal rewardPerHour) {
            this.rewardPerHour = rewardPerHour.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per day.
         *
         * @param rewardPerDay the new reward per day
         */
        public RewardBuilder rewardPerDay(BigDecimal rewardPerDay) {
            this.rewardPerDay = rewardPerDay.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per week.
         *
         * @param rewardPerWeek the new reward per week
         */
        public RewardBuilder rewardPerWeek(BigDecimal rewardPerWeek) {
            this.rewardPerWeek = rewardPerWeek.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per month.
         *
         * @param rewardPerMonth the new reward per month
         */
        public RewardBuilder rewardPerMonth(BigDecimal rewardPerMonth) {
            this.rewardPerMonth = rewardPerMonth.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per year.
         *
         * @param rewardPerYear the new reward per year
         */
        public RewardBuilder rewardPerYear(BigDecimal rewardPerYear) {
            this.rewardPerYear = rewardPerYear.stripTrailingZeros();
            return this;
        }

        /**
         * Returns created estimated rewards.
         *
         * @return created estimated rewards
         */
        public Reward build() {
            return new Reward(coinInfo,
                              totalHashrate,
                              rewardPerHour,
                              rewardPerDay,
                              rewardPerWeek,
                              rewardPerMonth,
                              rewardPerYear);
        }

    }

}
