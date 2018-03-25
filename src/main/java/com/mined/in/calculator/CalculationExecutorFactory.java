package com.mined.in.calculator;

import com.mined.in.calculator.whattomine.WhatToMineCalculationExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of mining calculation executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CalculationExecutorFactory {

    /**
     * Returns mining calculation executor.
     *
     * @param calculationType mining calculation type
     * @return mining calculation executor
     */
    public static CalculationExecutor getCalculationExecutor(CalculationType calculationType) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (calculationType) {
        case WHAT_TO_MINE: {
            return new WhatToMineCalculationExecutor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(calculationType.name());
        }
    }

}
