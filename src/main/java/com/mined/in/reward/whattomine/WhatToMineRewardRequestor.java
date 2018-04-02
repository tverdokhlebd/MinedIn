package com.mined.in.reward.whattomine;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;

import okhttp3.OkHttpClient;

/**
 * WhatToMine estimated reward requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineRewardRequestor implements RewardRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Ethereum reward lock. */
    private static final Lock ETHEREUM_LOCK = new ReentrantLock();
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
    public Reward requestEthereumReward(BigDecimal hashrate) throws RewardRequestorException {
        ETHEREUM_LOCK.lock();
        try {
            return new EthereumRequestor(httpClient, ENDPOINTS_UPDATE).request(hashrate);
        } finally {
            ETHEREUM_LOCK.unlock();
        }
    }

}
