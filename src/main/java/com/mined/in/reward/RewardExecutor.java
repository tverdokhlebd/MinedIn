package com.mined.in.reward;

import java.math.BigDecimal;

/**
 * Interface for retrieving estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface RewardExecutor {

    /**
     * Gets ETH estimated reward.
     *
     * @param hashrate reported total hashrate
     * @return ETH estimated reward
     * @throws RewardExecutorException if there is any error in request executing
     */
    Reward getETHReward(BigDecimal hashrate) throws RewardExecutorException;

}
