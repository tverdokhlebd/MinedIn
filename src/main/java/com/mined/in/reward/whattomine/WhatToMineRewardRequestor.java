package com.mined.in.reward.whattomine;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;

import okhttp3.OkHttpClient;

/**
 * Implementation of WhatToMine requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineRewardRequestor implements RewardRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** ETH reward lock. */
    private static final ReentrantLock ETH_LOCK = new ReentrantLock();
    /** Endpoints update. */
    private static final int ENDPOINTS_UPDATE = 4;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public WhatToMineRewardRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Reward getETHReward(BigDecimal hashrate) throws RewardRequestorException {
        ETH_LOCK.lock();
        try {
            return new ETHRewardRequestor(httpClient, ENDPOINTS_UPDATE).request(hashrate);
        } finally {
            ETH_LOCK.unlock();
        }
    }

}
