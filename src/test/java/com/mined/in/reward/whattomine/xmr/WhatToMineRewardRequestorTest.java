package com.mined.in.reward.whattomine.xmr;

import static com.mined.in.coin.CoinType.XMR;
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
 * Tests of WhatToMine ethereum requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class WhatToMineRewardRequestorTest {

    @Test
    public void testCorrectJsonResponse() throws RewardRequestorException {
        BigDecimal hashrate = new BigDecimal("2.58E+3");
        JSONObject response =
                new JSONObject("{\"id\":101,\"name\":\"Monero\",\"tag\":\"XMR\",\"algorithm\":\"CryptoNightV7\",\"block_time\":\"117.0\","
                        + "\"block_reward\":4.751686236781218,\"block_reward24\":4.75619526923898,\"block_reward3\":4.76079083722721,"
                        + "\"block_reward7\":4.7732726200653,\"last_block\":1556965,\"difficulty\":63597509474.0,\"difficulty24\":"
                        + "61723412963.2299,\"difficulty3\":60026133200.7569,\"difficulty7\":59478096320.6813,\"nethash\":543568457,"
                        + "\"exchange_rate\":0.030499,\"exchange_rate24\":0.0293349537815126,\"exchange_rate3\":0.0293327425982235,"
                        + "\"exchange_rate7\":0.0272758123748622,\"exchange_rate_vol\":1728.22364332,\"exchange_rate_curr\":\"BTC\","
                        + "\"market_cap\":\"$4,345,428,836\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.017160\","
                        + "\"btc_revenue\":\"0.00052338\",\"revenue\":\"$4.67\",\"cost\":\"$0.79\",\"profit\":\"$3.88\","
                        + "\"status\":\"Active\",\"lagging\":false,\"timestamp\":1524419500}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        Reward reward = rewardRequestor.requestMoneroReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(XMR, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(117), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(4.751686236781218), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(1556965), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(63597509474L), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(543568457), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000715), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.01716), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.12012), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.5148), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(6.2634), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        try {
            rewardRequestor.requestMoneroReward(BigDecimal.valueOf(2580));
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
            rewardRequestor.requestMoneroReward(BigDecimal.valueOf(2580));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
