package com.mined.in.reward.whattomine.eth;

import static com.mined.in.coin.CoinType.ETH;
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
 * Tests of WhatToMine ETH requestor.
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
                new JSONObject("{\"id\":151,\"name\":\"Ethereum\",\"tag\":\"ETH\",\"algorithm\":\"Ethash\",\"block_time\":\"14.5339\","
                        + "\"block_reward\":2.91,\"block_reward24\":2.91000000000001,\"block_reward3\":2.91,\"block_reward7\":2.91000000000001,"
                        + "\"last_block\":5386367,\"difficulty\":3.08359377889012e+15,\"difficulty24\":3.15113330321897e+15,\"difficulty3\""
                        + ":3.15333133159424e+15,\"difficulty7\":3.18760003087741e+15,\"nethash\":212165611356216,\"exchange_rate\":0.055872,"
                        + "\"exchange_rate24\":0.0558127599164927,\"exchange_rate3\":0.0551308601768426,\"exchange_rate7\":0.0556511829329325,"
                        + "\"exchange_rate_vol\":8433.85786587,\"exchange_rate_curr\":\"BTC\",\"market_cap\":\"$36,926,348,303\",\"pool_fee\":"
                        + "\"0.000000\",\"estimated_rewards\":\"0.013883\",\"btc_revenue\":\"0.00077568\",\"revenue\":\"$5.20\",\"cost\":"
                        + "\"$0.97\",\"profit\":\"$4.23\",\"status\":\"Active\",\"lagging\":false,\"timestamp\":1522951041}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        Reward reward = rewardRequestor.requestEthereumReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(ETH, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(14.5339), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(2.91), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(5386367), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(2.91), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(3.08359377889012e+15), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(212165611356216L), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000578), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.013883), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.097181), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.41649), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(5.067295), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        try {
            rewardRequestor.requestEthereumReward(BigDecimal.valueOf(174));
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
            rewardRequestor.requestEthereumReward(BigDecimal.valueOf(174));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
