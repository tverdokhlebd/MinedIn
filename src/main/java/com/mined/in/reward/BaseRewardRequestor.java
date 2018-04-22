package com.mined.in.reward;

import java.math.BigDecimal;
import java.util.Date;

import com.mined.in.coin.CoinInfo;
import com.mined.in.http.BaseRequestor;

/**
 * Base reward requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of the argument
 * @param <R> type of the result
 */
public interface BaseRewardRequestor<T, R> extends BaseRequestor<T, R> {

    /**
     * Gets base reward.
     *
     * @return base reward
     */
    BigDecimal getBaseReward();

    /**
     * Gets cached next update.
     *
     * @return cached next update
     */
    Date getCachedNextUpdate();

    /**
     * Sets cached next update.
     *
     * @param nextUpdate next update
     */
    void setCachedNextUpdate(Date nextUpdate);

    /**
     * Gets cached coin info.
     *
     * @return cached coin info
     */
    CoinInfo getCachedCoinInfo();

    /**
     * Sets cached coin info.
     *
     * @param coinInfo coin info
     */
    void setCachedCoinInfo(CoinInfo coinInfo);

    /**
     * Gets cached estimated reward per day.
     *
     * @return cached estimated reward per day
     */
    BigDecimal getCachedEstimatedRewardPerDay();

    /**
     * Sets cached estimated reward per day.
     *
     * @param estimatedRewardPerDay estimated reward per day
     */
    void setCachedEstimatedRewardPerDay(BigDecimal estimatedRewardPerDay);

}
