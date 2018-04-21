package com.mined.in.reward;

import com.mined.in.http.HttpClientFactory;
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
        OkHttpClient httpClient = HttpClientFactory.create();
        return create(rewardType, httpClient);
    }

    /**
     * Creates estimated reward requestor.
     *
     * @param rewardType estimated reward type
     * @param httpClient HTTP client
     * @return estimated reward requestor
     */
    public static RewardRequestor create(RewardType rewardType, OkHttpClient httpClient) {
        switch (rewardType) {
        case WHAT_TO_MINE: {
            return new WhatToMineRewardRequestor(httpClient);
        }
        default:
            throw new IllegalArgumentException(rewardType.name());
        }
    }

}
