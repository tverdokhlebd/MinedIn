package com.mined.in.reward.whattomine.etc;

import static com.mined.in.coin.CoinType.ETC;
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
 * Tests of WhatToMine ethereum classic requestor.
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
                new JSONObject("{\"id\":162,\"name\":\"EthereumClassic\",\"tag\":\"ETC\",\"algorithm\":\"Ethash\",\"block_time\":\"18.9511\""
                        + ",\"block_reward\":3.88,\"block_reward24\":3.88000000000005,\"block_reward3\":3.88000000000002,\"block_reward7\":"
                        + "3.88000000000001,\"last_block\":5771201,\"difficulty\":158727344186338.0,\"difficulty24\":153345826238172.0,"
                        + "\"difficulty3\":154950846698510.0,\"difficulty7\":158609538882375.0,\"nethash\":8375626965523,\"exchange_rate\""
                        + ":0.002257,\"exchange_rate24\":0.00214429201680672,\"exchange_rate3\":0.00221220497782446,\"exchange_rate7\":"
                        + "0.00219379317345359,\"exchange_rate_vol\":1858.27890792,\"exchange_rate_curr\":\"BTC\",\"market_cap\":"
                        + "\"$2,024,545,376\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.183632\",\"btc_revenue\":\"0.00041446\","
                        + "\"revenue\":\"$3.67\",\"cost\":\"$0.97\",\"profit\":\"$2.69\",\"status\":\"Active\",\"lagging\":false,"
                        + "\"timestamp\":1524768694}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        Reward reward = rewardRequestor.requestEthereumClassicReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(ETC, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(18.9511), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(3.88), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(5771201), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(158727344186338L), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(8375626965523L), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.015849), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.380380), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(2.66266), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(11.4114), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(138.8387), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE, httpClient);
        try {
            rewardRequestor.requestEthereumClassicReward(BigDecimal.valueOf(174));
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
            rewardRequestor.requestEthereumClassicReward(BigDecimal.valueOf(174));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
