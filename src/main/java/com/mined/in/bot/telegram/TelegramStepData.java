package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.ENTER_WALLET;
import static com.mined.in.bot.telegram.TelegramStepData.Step.START;

import com.mined.in.description.CoinInfoDescription;
import com.mined.in.description.CoinMarketDescription;
import com.mined.in.description.CoinRewardDescription;
import com.mined.in.description.CoinTypeDescription;
import com.mined.in.description.PoolTypeDescription;

/**
 * Data of current step.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramStepData {

    /** Current step. */
    private Step step;
    /** Selected coin type. */
    private CoinTypeDescription coinType;
    /** Selected coin info. */
    private CoinInfoDescription coinInfo;
    /** Selected pool info. */
    private PoolTypeDescription poolInfo;
    /** Selected market info. */
    private CoinMarketDescription marketInfo;
    /** Selected reward info. */
    private CoinRewardDescription rewardInfo;

    /**
     * Enumeration of steps.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum Step {

        START(0),
        ENTER_WALLET(0),
        SELECT_COIN_TYPE(1),
        SELECT_POOL_ACCOUNT(2),
        SELECT_COIN_INFO(3),
        SELECT_COIN_MARKET(4),
        SELECT_COIN_REWARD(5);

        /** Step position. */
        private int position;

        /**
         * Creates instance.
         *
         * @param position step position
         */
        private Step(int position) {
            this.position = position;
        }

        /**
         * Gets step by position.
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
            throw new IllegalArgumentException("No step with position " + position);
        }

    }

    /**
     * Creates instance.
     *
     * @param data simple text message or callback query data
     * @param simpleMessage {@code true} if it is simple text message, otherwise - callback query data
     */
    public TelegramStepData(String data, boolean simpleMessage) {
        super();
        if (simpleMessage) {
            step = data.equalsIgnoreCase("/start") ? START : ENTER_WALLET;
        } else {
            // "COIN_TYPE-POOL_ACCOUNT-COIN_INFO-COIN_MARKET-COIN_REWARD" ("ETH-DWARFPOOL-WHAT_TO_MINE-COIN_MARKET_CAP-WHAT_TO_MINE")
            String[] splittedData = data.split("-");
            int splittedDataLength = splittedData.length;
            if (splittedDataLength > 0) {
                coinType = CoinTypeDescription.valueOf(splittedData[0]);
            }
            if (splittedDataLength > 1) {
                poolInfo = PoolTypeDescription.valueOf(splittedData[1]);
            }
            if (splittedDataLength > 2) {
                coinInfo = CoinInfoDescription.valueOf(splittedData[2]);
            }
            if (splittedDataLength > 3) {
                marketInfo = CoinMarketDescription.valueOf(splittedData[3]);
            }
            if (splittedDataLength > 4) {
                rewardInfo = CoinRewardDescription.valueOf(splittedData[4]);
            }
            step = Step.getByPosition(splittedDataLength);
        }
    }

    /**
     * Gets callback query data.
     *
     * @return callback query data
     */
    public String getCallbackQueryData() {
        return coinType.name() + "-" + poolInfo.name() + "-" + coinInfo.name() + "-" + marketInfo.name() + "-" + rewardInfo.name();
    }

    /**
     * Gets step.
     *
     * @return step
     */
    public Step getStep() {
        return step;
    }

    /**
     * Gets coin type.
     *
     * @return coin type
     */
    public CoinTypeDescription getCoinType() {
        return coinType;
    }

    /**
     * Gets coin info.
     *
     * @return coin info
     */
    public CoinInfoDescription getCoinInfo() {
        return coinInfo;
    }

    /**
     * Gets pool info.
     *
     * @return pool info
     */
    public PoolTypeDescription getPoolInfo() {
        return poolInfo;
    }

    /**
     * Gets market info.
     *
     * @return market info
     */
    public CoinMarketDescription getMarketInfo() {
        return marketInfo;
    }

    /**
     * Gets reward info.
     *
     * @return reward info
     */
    public CoinRewardDescription getRewardInfo() {
        return rewardInfo;
    }

    /**
     * Sets step.
     *
     * @param step new step
     */
    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * Sets coin info.
     *
     * @param coinInfo new coin info
     */
    public void setCoinInfo(CoinInfoDescription coinInfo) {
        this.coinInfo = coinInfo;
    }

    /**
     * Sets market info.
     *
     * @param marketInfo new market info
     */
    public void setMarketInfo(CoinMarketDescription marketInfo) {
        this.marketInfo = marketInfo;
    }

    /**
     * Sets reward info.
     *
     * @param rewardInfo new reward info
     */
    public void setRewardInfo(CoinRewardDescription rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

}
