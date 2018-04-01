package com.mined.in.reward.whattomine;

import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinInfo.CoinInfoBuilder;
import com.mined.in.coin.CoinType;
import com.mined.in.reward.Reward;
import com.mined.in.reward.Reward.Builder;

/**
 * Utils for working with WhatToMine estimated reward requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RewardUtil {

    /** Hours in day. */
    private static final BigDecimal HOURS_IN_DAY = BigDecimal.valueOf(24);
    /** Days in week. */
    private static final BigDecimal DAYS_IN_WEEK = BigDecimal.valueOf(7);
    /** Days in month. */
    private static final BigDecimal DAYS_IN_MONTH = BigDecimal.valueOf(30);
    /** Days in year. */
    private static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);

    /**
     * Creates estimated reward from JSON response.
     *
     * @param coinType type of coin
     * @param hashrate reported total hashrate
     * @param jsonResponse JSON response
     * @return estimated reward
     */
    public static Reward createEstimatedReward(CoinType coinType, BigDecimal hashrate, JSONObject jsonResponse) {
        CoinInfo.CoinInfoBuilder coinBuilder = new CoinInfoBuilder();
        coinBuilder.coinType(coinType)
                   .blockTime(BigDecimal.valueOf(jsonResponse.getDouble("block_time")))
                   .blockReward(BigDecimal.valueOf(jsonResponse.getDouble("block_reward")))
                   .blockCount(BigDecimal.valueOf(jsonResponse.getDouble("last_block")))
                   .difficulty(BigDecimal.valueOf(jsonResponse.getDouble("difficulty")))
                   .networkHashrate(BigDecimal.valueOf(jsonResponse.getDouble("nethash")));
        CoinInfo coinInfo = coinBuilder.build();
        // Base rewards based on 84.0 MH/s
        BigDecimal estimatedRewardPerDay = BigDecimal.valueOf(jsonResponse.getDouble("estimated_rewards"));
        BigDecimal calculatedRewardPerDay =
                hashrate.multiply(estimatedRewardPerDay).divide(BigDecimal.valueOf(84), 6, DOWN);
        Reward.Builder rewardBuilder = new Builder();
        rewardBuilder.coinInfo(coinInfo)
                     .setTotalHashrate(hashrate)
                     .rewardPerHour(calculatedRewardPerDay.divide(HOURS_IN_DAY, DOWN))
                     .rewardPerDay(calculatedRewardPerDay)
                     .rewardPerWeek(calculatedRewardPerDay.multiply(DAYS_IN_WEEK))
                     .rewardPerMonth(calculatedRewardPerDay.multiply(DAYS_IN_MONTH))
                     .rewardPerYear(calculatedRewardPerDay.multiply(DAYS_IN_YEAR));
        return rewardBuilder.build();
    }

}
