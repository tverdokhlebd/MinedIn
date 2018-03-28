package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.START;
import static com.mined.in.bot.telegram.TelegramStepData.Step.WALLET;

import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketType;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.RewardType;

/**
 * Class for representing data of current step.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramStepData {

    /** Current step. */
    private Step step;
    /** Selected coin type. */
    private CoinType coinType;
    /** Selected pool type. */
    private PoolType poolType;
    /** Selected market type. */
    private MarketType marketType;
    /** Selected reward type. */
    private RewardType rewardType;

    /**
     * Enumeration of steps.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum Step {

        START(0),
        WALLET(0),
        COIN(1),
        POOL(2),
        MARKET(3),
        REWARD(4);

        /** Step position. */
        private int position;

        /**
         * Creates the instance of step.
         *
         * @param position step position
         */
        private Step(int position) {
            this.position = position;
        }

        /**
         * Returns step by position.
         *
         * @param position step position
         * @return step by position
         */
        public static Step getByPosition(int position) {
            Step[] currentStepArray = Step.values();
            for (int i = 0; i < currentStepArray.length; i++) {
                Step currentStep = currentStepArray[i];
                if (currentStep.position == position) {
                    return currentStep;
                }
            }
            throw new IllegalArgumentException("No steps with position " + position);
        }

    }

    /**
     * Creates the instance of current step data.
     *
     * @param data simple text message or callback query data
     * @param simpleMessage {@code true} if it is simple text message, otherwise - callback query data
     */
    public TelegramStepData(String data, boolean simpleMessage) {
        super();
        if (simpleMessage) {
            step = data.equalsIgnoreCase("/start") ? START : WALLET;
        } else {
            // Callback query data format: "COIN_POOL_MARKET_REWARD" ("ETH_Dwarfpool_CoinMarketCap_WhatToMine")
            String[] splittedData = data.split("_");
            int splittedDataLength = splittedData.length;
            if (splittedDataLength > 0) {
                coinType = CoinType.getBySymbol(splittedData[0]);
            }
            if (splittedDataLength > 1) {
                poolType = PoolType.getByName(splittedData[1]);
            }
            if (splittedDataLength > 2) {
                marketType = MarketType.getByName(splittedData[2]);
            }
            if (splittedDataLength > 3) {
                rewardType = RewardType.getByName(splittedData[3]);
            }
            step = Step.getByPosition(splittedDataLength);
        }
    }

    /**
     * Returns callback query data.
     *
     * @return callback query data
     */
    public String getCallbackQueryData() {
        return coinType.getSymbol() + "_" + poolType.getName() + "_" + marketType.getName() + "_" + rewardType.getName();
    }

    /**
     * Gets the step.
     *
     * @return the step
     */
    public Step getStep() {
        return step;
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
     * Gets the pool type.
     *
     * @return the pool type
     */
    public PoolType getPoolType() {
        return poolType;
    }

    /**
     * Gets the market type.
     *
     * @return the market type
     */
    public MarketType getMarketType() {
        return marketType;
    }

    /**
     * Gets the reward type.
     *
     * @return the reward type
     */
    public RewardType getRewardType() {
        return rewardType;
    }

    /**
     * Sets the step.
     *
     * @param step the new step
     */
    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * Sets the market type.
     *
     * @param marketType the new market type
     */
    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    /**
     * Sets the reward type.
     *
     * @param rewardType the new reward type
     */
    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

}
