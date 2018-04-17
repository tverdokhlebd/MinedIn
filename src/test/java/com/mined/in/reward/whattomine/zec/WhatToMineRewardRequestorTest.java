package com.mined.in.reward.whattomine.zec;

import static com.mined.in.coin.CoinType.ZEC;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
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
import com.mined.in.reward.whattomine.WhatToMineRewardRequestor;

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
        BigDecimal hashrate = BigDecimal.valueOf(1.74e+8);
        JSONObject response =
                new JSONObject("{\"id\":166,\"name\":\"Zcash\",\"tag\":\"ZEC\",\"algorithm\":\"Equihash\",\"block_time\":\"150.0\","
                        + "\"block_reward\":10.0,\"block_reward24\":10.0,\"block_reward3\":10.0,\"block_reward7\":10.0,\"last_block\""
                        + ":307762,\"difficulty\":9172726.75083478,\"difficulty24\":8649969.04064272,\"difficulty3\":8627478.63190333,"
                        + "\"difficulty7\":8698349.35610197,\"nethash\":500953183,\"exchange_rate\":0.027942,\"exchange_rate24\":"
                        + "0.0277767265135699,\"exchange_rate3\":0.0277603467764531,\"exchange_rate7\":0.0274782905827656,\"exchange_rate_vol\""
                        + ":596.714154084,\"exchange_rate_curr\":\"BTC\",\"market_cap\":\"$844,051,293\",\"pool_fee\":\"0.000000\","
                        + "\"estimated_rewards\":\"0.010608\",\"btc_revenue\":\"0.00029640\",\"revenue\":\"$2.41\",\"cost\":\"$0.86\","
                        + "\"profit\":\"$1.54\",\"status\":\"Active\",\"lagging\":false,\"timestamp\":1523984267}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        Reward reward = rewardRequestor.requestZcashReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(ZEC, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(new BigDecimal("1.5E+2"), coinInfo.getBlockTime());
        assertEquals(new BigDecimal("1E+1"), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(307762), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(9172726.75083478), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(500953183), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000915), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.021973), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.153811), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.65919), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(8.020145), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
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
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        try {
            rewardRequestor.requestZcashReward(BigDecimal.valueOf(174));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
