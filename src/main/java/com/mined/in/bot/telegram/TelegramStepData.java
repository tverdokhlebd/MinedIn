package com.mined.in.bot.telegram;

import com.mined.in.coin.Coin;
import com.mined.in.exchanger.Exchanger;
import com.mined.in.pool.Pool;

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
    private Coin coin;
    /** Selected pool. */
    private Pool pool;
    /** Selected exchanger. */
    private Exchanger exchanger;

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
        POOL(2),
        /** The exchanger. */
        EXCHANGER(3);

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
        // Callback query data format: "COIN_POOL_EXCHANGER" ("ETH_dwarfpool_exmo")
        String[] splittedData = data.split("_");
        int splittedDataLength = splittedData.length;
        if (splittedDataLength > 0) {
            coin = Coin.getBySymbol(splittedData[0]);
        }
        if (splittedDataLength > 1) {
            pool = Pool.getByName(splittedData[1]);
        }
        if (splittedDataLength > 2) {
            exchanger = Exchanger.getByName(splittedData[2]);
        }
        step = Step.getByPosition(splittedDataLength);
    }

    /**
     * Returns string representation of callback query data.
     *
     * @return string representation of callback query data
     */
    public String getCallbackQueryData() {
        return coin.getSymbol() + "_" + pool.getName() + "_" + exchanger.getName();
    }

    /**
     * Gets the coin.
     *
     * @return the coin
     */
    public Coin getCoin() {
        return coin;
    }

    /**
     * Gets the pool.
     *
     * @return the pool
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Gets the exchanger.
     *
     * @return the exchanger
     */
    public Exchanger getExchanger() {
        return exchanger;
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
