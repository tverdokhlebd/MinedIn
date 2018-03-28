package com.mined.in.reward;

import java.math.BigDecimal;

/**
 * Interface for retrieving estimated rewards.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface RewardExecutor {

    /**
     * Returns ETH estimated rewards.
     *
     * @param hashrate reported total hashrate
     * @return ETH estimated rewards
     * @throws RewardExecutorException if there is any error in request executing
     */
    Reward getETHReward(BigDecimal hashrate) throws RewardExecutorException;

}
