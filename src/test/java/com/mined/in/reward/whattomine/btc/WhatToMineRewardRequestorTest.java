package com.mined.in.reward.whattomine.btc;

import static com.mined.in.coin.CoinType.BTC;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.reward.RewardType.WHAT_TO_MINE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.coin.CoinInfo;
import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.reward.RewardRequestorFactory;

import okhttp3.OkHttpClient;

/**
 * Tests of WhatToMine bitcoin requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class WhatToMineRewardRequestorTest {

    @Test
    public void testCorrectJsonResponse() throws RewardRequestorException {
        BigDecimal hashrate = BigDecimal.valueOf(1.4e+13);
        JSONObject response =
                new JSONObject("{\"id\":1,\"name\":\"Bitcoin\",\"tag\":\"BTC\",\"algorithm\":\"SHA-256\",\"block_time\":\"525.0\","
                        + "\"block_reward\":12.6269,\"block_reward24\":12.6674244444444,\"block_reward3\":12.679997037037,\"block_reward7\""
                        + ":12.6708969444444,\"last_block\":519450,\"difficulty\":3839316899029.7,\"difficulty24\":3839316899029.69,"
                        + "\"difficulty3\":3839316899029.7,\"difficulty7\":3839316899029.69,\"nethash\":31409029562500380952,"
                        + "\"exchange_rate\":8930.7,\"exchange_rate24\":8860.32468085106,\"exchange_rate3\":8657.85162972813,"
                        + "\"exchange_rate7\":8338.28232827351,\"exchange_rate_vol\":30714.97685345,\"exchange_rate_curr\":"
                        + "\"BTC\",\"market_cap\":\"$151,759,450,326\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.000926\","
                        + "\"btc_revenue\":\"0.00092624\",\"revenue\":\"$8.27\",\"cost\":\"$3.29\",\"profit\":\"$4.98\",\"status\":"
                        + "\"Active\",\"lagging\":false,\"timestamp\":1524417868}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        Reward reward = rewardRequestor.requestBitcoinReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(BTC, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(525), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(12.6269), coinInfo.getBlockReward());
        assertEquals(new BigDecimal("5.1945e+5"), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(3839316899029.7), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(3.1409029562500380952e+19), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000038), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.000926), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.006482), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.02778), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(0.33799), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        try {
            rewardRequestor.requestBitcoinReward(BigDecimal.valueOf(1.4e+13));
        } catch (RewardRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = RewardRequestorException.class)
    public void test500HttpError() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        try {
            rewardRequestor.requestBitcoinReward(BigDecimal.valueOf(1.4e+13));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
