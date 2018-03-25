package com.mined.in.calculator.whattomine.eth;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.calculator.Calculation;
import com.mined.in.calculator.CalculationExecutor;
import com.mined.in.calculator.CalculationExecutorException;
import com.mined.in.calculator.whattomine.WhatToMineCalculationExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of WhatToMine ethereum executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class WhatToMineCalculationExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws CalculationExecutorException {
        BigDecimal hashrate = BigDecimal.valueOf(174);
        JSONObject response =
                new JSONObject("{\"id\":151,\"name\":\"Ethereum\",\"tag\":\"ETH\",\"algorithm\":\"Ethash\",\"block_time\":\"14.4406\","
                        + "\"block_reward\":2.91,\"block_reward24\":2.91000000000001,\"block_reward3\":2.91,\"block_reward7\":2.91,"
                        + "\"last_block\":5319532,\"difficulty\":3.23405110864068e+15,\"difficulty24\":3.28768043075892e+15,"
                        + "\"difficulty3\":3.26138538264012e+15,\"difficulty7\":3.26159879702061e+15,\"nethash\":223955452587889,"
                        + "\"exchange_rate\":0.061054,\"exchange_rate24\":0.0607868093126386,\"exchange_rate3\":0.0611831939414956,"
                        + "\"exchange_rate7\":0.062520153768632,\"exchange_rate_vol\":7094.59432922,\"exchange_rate_curr\":\"BTC\","
                        + "\"market_cap\":\"$51,261,485,391\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.006424\","
                        + "\"btc_revenue\":\"0.00039220\",\"revenue\":\"$3.35\",\"cost\":\"$0.97\",\"profit\":\"$2.37\","
                        + "\"status\":\"Active\",\"lagging\":false,\"timestamp\":1521986783}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(httpClient);
        Calculation calculation = calculationExecutor.getEthereumCalculation(hashrate);
        assertEquals(ETH, calculation.getCoinType());
        assertEquals(hashrate, calculation.getTotalHashrate());
        assertEquals(BigDecimal.valueOf(14.4406), calculation.getBlockTime());
        assertEquals(BigDecimal.valueOf(2.91), calculation.getBlockReward());
        assertEquals(BigDecimal.valueOf(5319532), calculation.getBlockCount());
        assertEquals(BigDecimal.valueOf(3.23405110864068e+15), calculation.getDifficulty());
        assertEquals(BigDecimal.valueOf(223955452587889L), calculation.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000554), calculation.getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.013306), calculation.getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.093142), calculation.getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.39918), calculation.getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(4.85669), calculation.getRewardPerYear());
    }

    @Test(expected = CalculationExecutorException.class)
    public void testEmptyResponse() throws CalculationExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(httpClient);
        try {
            calculationExecutor.getEthereumCalculation(BigDecimal.valueOf(174));
        } catch (CalculationExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = CalculationExecutorException.class)
    public void test500HttpError() throws CalculationExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(httpClient);
        try {
            calculationExecutor.getEthereumCalculation(BigDecimal.valueOf(174));
        } catch (CalculationExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
