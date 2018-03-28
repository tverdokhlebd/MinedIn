package com.mined.in.reward;

import com.mined.in.reward.whattomine.WhatToMineRewardExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating estimated reward executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RewardExecutorFactory {

    /**
     * Creates estimated reward executor.
     *
     * @param rewardType estimated reward type
     * @return estimated reward executor
     */
    public static RewardExecutor create(RewardType rewardType) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (rewardType) {
        case WHAT_TO_MINE: {
            return new WhatToMineRewardExecutor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(rewardType.name());
        }
    }

}
