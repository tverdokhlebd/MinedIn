package com.mined.in.bot.telegram;

import com.mined.in.coin.CoinType;
import com.mined.in.pool.PoolType;

/**
 * Class for representing data of current step.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramStepData {

    /** Current step. */
    private final Step step;
    /** Selected coin. */
    private CoinType coin;
    /** Selected pool. */
    private PoolType pool;

    /**
     * Enumeration of steps.
     *
     * @author Dmitry Tverdokhleb
     *
     */
    public static enum Step {

        /** The coin. */
        COIN(1),
        /** The pool. */
        POOL(2);

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
         * @return step
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
     * @param data callback query data
     */
    public TelegramStepData(String data) {
        super();
        // Callback query data format: "COIN_POOL" ("ETH_dwarfpool")
        String[] splittedData = data.split("_");
        int splittedDataLength = splittedData.length;
        if (splittedDataLength > 0) {
            coin = CoinType.getBySymbol(splittedData[0]);
        }
        if (splittedDataLength > 1) {
            pool = PoolType.getByName(splittedData[1]);
        }
        step = Step.getByPosition(splittedDataLength);
    }

    /**
     * Returns string representation of callback query data.
     *
     * @return string representation of callback query data
     */
    public String getCallbackQueryData() {
        return coin.getSymbol() + "_" + pool.getName();
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
     * Gets the pool.
     *
     * @return the pool
     */
    public PoolType getPool() {
        return pool;
    }

    /**
     * Gets the step.
     *
     * @return the step
     */
    public Step getStep() {
        return step;
    }

}
