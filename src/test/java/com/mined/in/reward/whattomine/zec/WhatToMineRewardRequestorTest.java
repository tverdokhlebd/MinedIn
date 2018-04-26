package com.mined.in.reward.whattomine.zec;

import static com.mined.in.coin.CoinType.ZEC;
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
 * Tests of WhatToMine zcash requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class WhatToMineRewardRequestorTest {

    @Test
    public void testCorrectJsonResponse() throws RewardRequestorException {
        BigDecimal hashrate = new BigDecimal("1.e+3");
        JSONObject response =
                new JSONObject("{\"id\":166,\"name\":\"Zcash\",\"tag\":\"ZEC\",\"algorithm\":\"Equihash\",\"block_time\":\"151.0\","
                        + "\"block_reward\":10.0,\"block_reward24\":10.0,\"block_reward3\":10.0,\"block_reward7\":10.0,\"last_block\":"
                        + "312967,\"difficulty\":9391076.04078399,\"difficulty24\":8955899.53125816,\"difficulty3\":8895396.70129801,"
                        + "\"difficulty7\":8865165.64814038,\"nethash\":509481423,\"exchange_rate\":0.032195,\"exchange_rate24\":"
                        + "0.0321236855345912,\"exchange_rate3\":0.0323478917059749,\"exchange_rate7\":0.0316160893025607,\"exchange_rate_vol\":"
                        + "737.677228758,\"exchange_rate_curr\":\"BTC\",\"market_cap\":\"$1,078,735,713\",\"pool_fee\":\"0.000000\","
                        + "\"estimated_rewards\":\"0.010245\",\"btc_revenue\":\"0.00032985\",\"revenue\":\"$2.92\",\"cost\":\"$0.86\","
                        + "\"profit\":\"$2.05\",\"status\":\"Active\",\"lagging\":false,\"timestamp\":1524768515}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        Reward reward = rewardRequestor.requestZcashReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(ZEC, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(151), coinInfo.getBlockTime());
        assertEquals(new BigDecimal("1E+1"), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(312967), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(9391076.04078399), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(509481423), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000490), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.011775), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.082425), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.35325), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(4.297875), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        try {
            rewardRequestor.requestZcashReward(BigDecimal.valueOf(174));
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
            rewardRequestor.requestZcashReward(BigDecimal.valueOf(174));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
