package com.mined.in.reward.whattomine;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardExecutor;
import com.mined.in.reward.RewardExecutorException;

import okhttp3.OkHttpClient;

/**
 * Implementation of WhatToMine executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineRewardExecutor implements RewardExecutor {

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
    public WhatToMineRewardExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Reward getETHReward(BigDecimal hashrate) throws RewardExecutorException {
        ETH_LOCK.lock();
        try {
            return new ETHRewardRequestor(httpClient, ENDPOINTS_UPDATE).request(hashrate);
        } finally {
            ETH_LOCK.unlock();
        }
    }

}
