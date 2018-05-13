package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.ENTERED_WALLET;
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
    /** Selected pool type. */
    private PoolTypeDescription poolType;
    /** Selected coin market. */
    private CoinMarketDescription coinMarket;
    /** Selected coin reward. */
    private CoinRewardDescription coinReward;

    /**
     * Enumeration of steps.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum Step {

        START,
        ENTERED_WALLET,
        SELECTED_COIN_TYPE,
        SELECTED_POOL_ACCOUNT,
        SELECTED_COIN_INFO,
        SELECTED_COIN_MARKET,
        SELECTED_COIN_REWARD;

        /**
         * Gets step by position.
         *
         * @param position step position
         * @return step by position
         */
        public static Step getByPosition(int position) {
            Step[] stepArray = Step.values();
            if (position < 0 || position > stepArray.length) {
                throw new IllegalArgumentException("No step with position " + position);
            }
            return stepArray[position];
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
            step = data.equalsIgnoreCase("/start") ? START : ENTERED_WALLET;
        } else {
            // "COIN_TYPE-POOL_ACCOUNT-COIN_INFO-COIN_MARKET-COIN_REWARD" ("ETH-DWARFPOOL-WHAT_TO_MINE-COIN_MARKET_CAP-WHAT_TO_MINE")
            String[] splittedData = data.split("-");
            int splittedDataLength = splittedData.length;
            if (splittedDataLength > 0) {
                coinType = CoinTypeDescription.valueOf(splittedData[0]);
            }
            if (splittedDataLength > 1) {
                poolType = PoolTypeDescription.valueOf(splittedData[1]);
            }
            if (splittedDataLength > 2) {
                coinInfo = CoinInfoDescription.valueOf(splittedData[2]);
            }
            if (splittedDataLength > 3) {
                coinMarket = CoinMarketDescription.valueOf(splittedData[3]);
            }
            if (splittedDataLength > 4) {
                coinReward = CoinRewardDescription.valueOf(splittedData[4]);
            }
            // Skip START and ENTERED_WALLET steps
            step = Step.getByPosition(splittedDataLength + 1);
        }
    }

    /**
     * Gets callback query data.
     *
     * @return callback query data
     */
    public String getCallbackQueryData() {
        return coinType.name() + "-" + poolType.name() + "-" + coinInfo.name() + "-" + coinMarket.name() + "-" + coinReward.name();
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
     * Gets pool type.
     *
     * @return pool type
     */
    public PoolTypeDescription getPoolType() {
        return poolType;
    }

    /**
     * Gets coin market.
     *
     * @return coin market
     */
    public CoinMarketDescription getCoinMarket() {
        return coinMarket;
    }

    /**
     * Gets coin reward.
     *
     * @return coin reward
     */
    public CoinRewardDescription getCoinReward() {
        return coinReward;
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
     * Sets coin market.
     *
     * @param coinMarket new coin market
     */
    public void setMarketInfo(CoinMarketDescription coinMarket) {
        this.coinMarket = coinMarket;
    }

    /**
     * Sets coin reward.
     *
     * @param coinReward new coin reward
     */
    public void setRewardInfo(CoinRewardDescription coinReward) {
        this.coinReward = coinReward;
    }

}
