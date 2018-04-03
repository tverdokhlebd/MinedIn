package com.mined.in.reward;

import java.math.BigDecimal;

/**
 * Interface for requesting estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface RewardRequestor {

    /**
     * Requests estimated reward for ethereum.
     *
     * @param hashrate reported hashrate in H/s
     * @return estimated reward for ethereum
     * @throws RewardRequestorException if there is any error in request executing
     */
    Reward requestEthereumReward(BigDecimal hashrate) throws RewardRequestorException;

}
