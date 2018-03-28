package com.mined.in.reward.whattomine;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static java.math.RoundingMode.DOWN;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinInfo.CoinInfoBuilder;
import com.mined.in.coin.CoinType;
import com.mined.in.reward.Reward;
import com.mined.in.reward.Reward.RewardBuilder;
import com.mined.in.reward.RewardExecutor;
import com.mined.in.reward.RewardExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of WhatToMine executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineRewardExecutor implements RewardExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** WhatToMine API ETH url. */
    private static final String API_ETH_URL = "https://whattomine.com/coins/151.json";
    /** Hours in day. */
    private static final BigDecimal HOURS_IN_DAY = BigDecimal.valueOf(24);
    /** Days in week. */
    private static final BigDecimal DAYS_IN_WEEK = BigDecimal.valueOf(7);
    /** Days in month. */
    private static final BigDecimal DAYS_IN_MONTH = BigDecimal.valueOf(30);
    /** Days in year. */
    private static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);

    /**
     * Creates the WhatToMine executor instance.
     *
     * @param httpClient HTTP client
     */
    public WhatToMineRewardExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Reward getETHReward(BigDecimal hashrate) throws RewardExecutorException {
        Request request = new Request.Builder().url(API_ETH_URL).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RewardExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                return createEstimatedRewards(ETH, hashrate, jsonResponse);
            }
        } catch (JSONException e) {
            throw new RewardExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new RewardExecutorException(HTTP_ERROR, e);
        }
    }

    /**
     * Creates estimated rewards from JSON response.
     *
     * @param coinType type of coin
     * @param hashrate reported total hashrate
     * @param jsonResponse JSON response
     * @return estimated rewards from JSON response
     */
    private Reward createEstimatedRewards(CoinType coinType, BigDecimal hashrate, JSONObject jsonResponse) {
        CoinInfo.CoinInfoBuilder coinBuilder = new CoinInfoBuilder();
        coinBuilder.coinType(coinType)
                   .blockTime(BigDecimal.valueOf(jsonResponse.getDouble("block_time")))
                   .blockReward(BigDecimal.valueOf(jsonResponse.getDouble("block_reward")))
                   .blockCount(BigDecimal.valueOf(jsonResponse.getDouble("last_block")))
                   .difficulty(BigDecimal.valueOf(jsonResponse.getDouble("difficulty")))
                   .networkHashrate(BigDecimal.valueOf(jsonResponse.getDouble("nethash")));
        CoinInfo coinInfo = coinBuilder.createCoinInfo();
        // Base rewards based on 84.0 MH/s
        BigDecimal estimatedRewardPerDay = BigDecimal.valueOf(jsonResponse.getDouble("estimated_rewards"));
        BigDecimal calculatedRewardPerDay =
                hashrate.multiply(estimatedRewardPerDay).divide(BigDecimal.valueOf(84), 6, DOWN);
        Reward.RewardBuilder builder = new RewardBuilder();
        builder.coinInfo(coinInfo)
               .rewardPerHour(calculatedRewardPerDay.divide(HOURS_IN_DAY, DOWN))
               .rewardPerDay(calculatedRewardPerDay)
               .rewardPerWeek(calculatedRewardPerDay.multiply(DAYS_IN_WEEK))
               .rewardPerMonth(calculatedRewardPerDay.multiply(DAYS_IN_MONTH))
               .rewardPerYear(calculatedRewardPerDay.multiply(DAYS_IN_YEAR));
        return builder.build();
    }

}
