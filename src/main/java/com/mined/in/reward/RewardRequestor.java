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

    /**
     * Requests estimated reward for ethereum classic.
     *
     * @param hashrate reported hashrate in H/s
     * @return estimated reward for ethereum classic
     * @throws RewardRequestorException if there is any error in request executing
     */
    Reward requestEthereumClassicReward(BigDecimal hashrate) throws RewardRequestorException;

    /**
     * Requests estimated reward for zcash.
     *
     * @param hashrate reported hashrate in H/s
     * @return estimated reward for zcash
     * @throws RewardRequestorException if there is any error in request executing
     */
    Reward requestZcashReward(BigDecimal hashrate) throws RewardRequestorException;

}
