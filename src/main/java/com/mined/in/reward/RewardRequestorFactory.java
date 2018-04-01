package com.mined.in.reward;

import com.mined.in.reward.whattomine.WhatToMineRewardRequestor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating estimated reward requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RewardRequestorFactory {

    /**
     * Creates estimated reward requestor.
     *
     * @param rewardType estimated reward type
     * @return estimated reward requestor
     */
    public static RewardRequestor create(RewardType rewardType) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        switch (rewardType) {
        case WHAT_TO_MINE: {
            return new WhatToMineRewardRequestor(okHttpBuilder.build());
        }
        default:
            throw new IllegalArgumentException(rewardType.name());
        }
    }

}
