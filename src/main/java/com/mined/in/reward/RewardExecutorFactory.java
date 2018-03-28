package com.mined.in.reward;

import com.mined.in.reward.whattomine.WhatToMineRewardExecutor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating of estimated rewards executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RewardExecutorFactory {

    /**
     * Returns estimated rewards executor.
     *
     * @param rewardType estimated rewards type
     * @return estimated rewards executor
     */
    public static RewardExecutor getRewardExecutor(RewardType rewardType) {
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
