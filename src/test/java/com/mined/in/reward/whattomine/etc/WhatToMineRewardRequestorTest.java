package com.mined.in.reward.whattomine.etc;

import static com.mined.in.coin.CoinType.ETC;
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
                new JSONObject("{\"id\":162,\"name\":\"EthereumClassic\",\"tag\":\"ETC\",\"algorithm\":\"Ethash\",\"block_time\":\"18.6099\""
                        + ",\"block_reward\":3.88,\"block_reward24\":3.88000000000005,\"block_reward3\":3.88000000000002,\"block_reward7\""
                        + ":3.88000000000001,\"last_block\":5729201,\"difficulty\":162837230709622.0,\"difficulty24\":166648535529213.0,"
                        + "\"difficulty3\":165472360541630.0,\"difficulty7\":166698095551878.0,\"nethash\":8750032547709,\"exchange_rate\""
                        + ":0.002036,\"exchange_rate24\":0.00201371428571428,\"exchange_rate3\":0.00200533392857143,\"exchange_rate7\":"
                        + "0.00201148772079007,\"exchange_rate_vol\":876.78551452,\"exchange_rate_curr\":\"BTC\",\"market_cap\":"
                        + "\"$1,671,335,194\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.168982\",\"btc_revenue\":\"0.00034403\","
                        + "\"revenue\":\"$2.79\",\"cost\":\"$0.97\",\"profit\":\"$1.82\",\"status\":\"Active\",\"lagging\":false,"
                        + "\"timestamp\":1523983523}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        Reward reward = rewardRequestor.requestEthereumClassicReward(hashrate);
        CoinInfo coinInfo = reward.getCoinInfo();
        assertEquals(ETC, coinInfo.getCoinType());
        assertEquals(hashrate, reward.getReportedHashrate());
        assertEquals(BigDecimal.valueOf(18.6099), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(3.88), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(5729201), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(162837230709622.0), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(8750032547709L), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.014584), reward.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.350034), reward.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(2.450238), reward.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(10.50102), reward.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(127.76241), reward.getRewardPerYear());
    }

    @Test(expected = RewardRequestorException.class)
    public void testEmptyResponse() throws RewardRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
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
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(httpClient);
        try {
            rewardRequestor.requestEthereumClassicReward(BigDecimal.valueOf(174));
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
