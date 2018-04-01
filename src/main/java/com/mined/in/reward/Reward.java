package com.mined.in.reward;

import java.math.BigDecimal;

import com.mined.in.coin.CoinInfo;

/**
 * Estimated reward according to reported hashrate of pool account. It uses as intermediate result of calculations.
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
            BigDecimal rewardPerMonth, BigDecimal rewardPerYear) {
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
     * Builder of estimated reward.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static class Builder {

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
        public Builder() {
            super();
        }

        /**
         * Sets the coin info.
         *
         * @param coinInfo the new coin info
         * @return builder of estimated reward
         */
        public Builder coinInfo(CoinInfo coinInfo) {
            this.coinInfo = coinInfo;
            return this;
        }

        /**
         * Sets the total hashrate.
         *
         * @param totalHashrate the new total hashrate
         * @return builder of estimated reward
         */
        public Builder setTotalHashrate(BigDecimal totalHashrate) {
            this.totalHashrate = totalHashrate.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per hour.
         *
         * @param rewardPerHour the new reward per hour
         * @return builder of estimated reward
         */
        public Builder rewardPerHour(BigDecimal rewardPerHour) {
            this.rewardPerHour = rewardPerHour.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per day.
         *
         * @param rewardPerDay the new reward per day
         * @return builder of estimated reward
         */
        public Builder rewardPerDay(BigDecimal rewardPerDay) {
            this.rewardPerDay = rewardPerDay.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per week.
         *
         * @param rewardPerWeek the new reward per week
         * @return builder of estimated reward
         */
        public Builder rewardPerWeek(BigDecimal rewardPerWeek) {
            this.rewardPerWeek = rewardPerWeek.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per month.
         *
         * @param rewardPerMonth the new reward per month
         * @return builder of estimated reward
         */
        public Builder rewardPerMonth(BigDecimal rewardPerMonth) {
            this.rewardPerMonth = rewardPerMonth.stripTrailingZeros();
            return this;
        }

        /**
         * Sets the reward per year.
         *
         * @param rewardPerYear the new reward per year
         * @return builder of estimated reward
         */
        public Builder rewardPerYear(BigDecimal rewardPerYear) {
            this.rewardPerYear = rewardPerYear.stripTrailingZeros();
            return this;
        }

        /**
         * Build estimated reward.
         *
         * @return estimated reward
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
