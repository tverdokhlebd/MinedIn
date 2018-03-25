package com.mined.in.worker;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.calculation.CalculationExecutor;
import com.mined.in.calculation.CalculationExecutorException;
import com.mined.in.calculation.whattomine.WhatToMineCalculationExecutor;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;
import com.mined.in.market.coinmarketcap.CoinMarketCapMarketExecutor;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.dwarfpool.DwarfpoolAccountExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of ETH mined worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ETHMinedWorkerTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        BigDecimal walletBalance = BigDecimal.valueOf(0.78665394);
        JSONObject poolResponseJSON =
                new JSONObject("{ \"autopayout_from\": \"5.000\", \"earning_24_hours\": \"0.01137842\", \"error\": false, "
                        + "\"immature_earning\": 0.000455540976, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                        + "\"Sat, 24 Mar 2018 21:09:25 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 174, "
                        + "\"total_hashrate_calculated\": 197.03, \"transferring_to_balance\": 0, \"wallet\": "
                        + "\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\", \"wallet_balance\": \"0.78665394\", \"workers\": { \"dmtry\": "
                        + "{ \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 21:09:32 GMT\", \"second_since_submit\": 342, \"worker\": \"dmtry\" }, "
                        + "\"tv\": { \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 20:06:25 GMT\", \"second_since_submit\": 305, \"worker\": \"tv\" } } }");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        BigDecimal coinPrice = BigDecimal.valueOf(536.854);
        JSONObject marketJSON =
                new JSONObject("{ \"id\": \"ethereum\", \"name\": \"Ethereum\", \"symbol\": \"ETH\", \"rank\": \"2\", \"price_usd\": \"536.854\", "
                        + "\"price_btc\": \"0.0619693\", \"24h_volume_usd\": \"1560240000.0\", \"market_cap_usd\": \"52799438836.0\", "
                        + "\"available_supply\": \"98349717.0\", \"total_supply\": \"98349717.0\", \"max_supply\": null, "
                        + "\"percent_change_1h\": \"0.94\", \"percent_change_24h\": \"-4.19\", \"percent_change_7d\": \"-11.61\", "
                        + "\"last_updated\": \"1521743654\" }");
        JSONArray marketArray = new JSONArray();
        marketArray.put(marketJSON);
        OkHttpClient marketHttpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(marketHttpClient);
        JSONObject calculationResponse =
                new JSONObject("{\"id\":151,\"name\":\"Ethereum\",\"tag\":\"ETH\",\"algorithm\":\"Ethash\",\"block_time\":\"14.4406\","
                        + "\"block_reward\":2.91,\"block_reward24\":2.91000000000001,\"block_reward3\":2.91,\"block_reward7\":2.91,"
                        + "\"last_block\":5319532,\"difficulty\":3.23405110864068e+15,\"difficulty24\":3.28768043075892e+15,"
                        + "\"difficulty3\":3.26138538264012e+15,\"difficulty7\":3.26159879702061e+15,\"nethash\":223955452587889,"
                        + "\"exchange_rate\":0.061054,\"exchange_rate24\":0.0607868093126386,\"exchange_rate3\":0.0611831939414956,"
                        + "\"exchange_rate7\":0.062520153768632,\"exchange_rate_vol\":7094.59432922,\"exchange_rate_curr\":\"BTC\","
                        + "\"market_cap\":\"$51,261,485,391\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.006424\","
                        + "\"btc_revenue\":\"0.00039220\",\"revenue\":\"$3.35\",\"cost\":\"$0.97\",\"profit\":\"$2.37\","
                        + "\"status\":\"Active\",\"lagging\":false,\"timestamp\":1521986783}");
        OkHttpClient calculationHttpClient = Utils.getHttpClient(calculationResponse.toString(), 200);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(calculationHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, marketExecutor, calculationExecutor);
        MinedResult result = worker.calculate(WALLET_ADDRESS);
        assertEquals(walletBalance, result.getCoinBalance());
        assertEquals(walletBalance.multiply(coinPrice), result.getUsdBalance());
        assertEquals(coinPrice, result.getCoinPrice());
        assertEquals(ETH, result.getCalculation().getCoinType());
        assertEquals(BigDecimal.valueOf(174), result.getCalculation().getTotalHashrate());
        assertEquals(BigDecimal.valueOf(14.4406), result.getCalculation().getBlockTime());
        assertEquals(BigDecimal.valueOf(2.91), result.getCalculation().getBlockReward());
        assertEquals(BigDecimal.valueOf(5319532), result.getCalculation().getBlockCount());
        assertEquals(BigDecimal.valueOf(3.23405110864068e+15), result.getCalculation().getDifficulty());
        assertEquals(BigDecimal.valueOf(223955452587889L), result.getCalculation().getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000554), result.getCalculation().getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.013306), result.getCalculation().getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.093142), result.getCalculation().getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.39918), result.getCalculation().getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(4.85669), result.getCalculation().getRewardPerYear());
    }

    @Test(expected = AccountExecutorException.class)
    public void testPoolError() throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        JSONObject poolResponseJSON = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient marketHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(marketHttpClient);
        OkHttpClient calculationHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(calculationHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, marketExecutor, calculationExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("API_DOWN", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MarketExecutorException.class)
    public void testMarketError() throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        JSONObject poolResponseJSON =
                new JSONObject("{ \"autopayout_from\": \"5.000\", \"earning_24_hours\": \"0.01137842\", \"error\": false, "
                        + "\"immature_earning\": 0.000455540976, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                        + "\"Sat, 24 Mar 2018 21:09:25 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 174.03, "
                        + "\"total_hashrate_calculated\": 197.03, \"transferring_to_balance\": 0, \"wallet\": "
                        + "\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\", \"wallet_balance\": \"0.78665394\", \"workers\": { \"dmtry\": "
                        + "{ \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 21:09:32 GMT\", \"second_since_submit\": 342, \"worker\": \"dmtry\" }, "
                        + "\"tv\": { \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 20:06:25 GMT\", \"second_since_submit\": 305, \"worker\": \"tv\" } } }");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient marketHttpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(marketHttpClient);
        OkHttpClient calculationHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(calculationHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, marketExecutor, calculationExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (MarketExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = CalculationExecutorException.class)
    public void testCalculationError() throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        JSONObject poolResponseJSON =
                new JSONObject("{ \"autopayout_from\": \"5.000\", \"earning_24_hours\": \"0.01137842\", \"error\": false, "
                        + "\"immature_earning\": 0.000455540976, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                        + "\"Sat, 24 Mar 2018 21:09:25 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 174.03, "
                        + "\"total_hashrate_calculated\": 197.03, \"transferring_to_balance\": 0, \"wallet\": "
                        + "\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\", \"wallet_balance\": \"0.78665394\", \"workers\": { \"dmtry\": "
                        + "{ \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 21:09:32 GMT\", \"second_since_submit\": 342, \"worker\": \"dmtry\" }, "
                        + "\"tv\": { \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                        + "\"last_submit\": \"Sat, 24 Mar 2018 20:06:25 GMT\", \"second_since_submit\": 305, \"worker\": \"tv\" } } }");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        JSONObject marketJSON =
                new JSONObject("{ \"id\": \"ethereum\", \"name\": \"Ethereum\", \"symbol\": \"ETH\", \"rank\": \"2\", \"price_usd\": \"536.854\", "
                        + "\"price_btc\": \"0.0619693\", \"24h_volume_usd\": \"1560240000.0\", \"market_cap_usd\": \"52799438836.0\", "
                        + "\"available_supply\": \"98349717.0\", \"total_supply\": \"98349717.0\", \"max_supply\": null, "
                        + "\"percent_change_1h\": \"0.94\", \"percent_change_24h\": \"-4.19\", \"percent_change_7d\": \"-11.61\", "
                        + "\"last_updated\": \"1521743654\" }");
        JSONArray marketArray = new JSONArray();
        marketArray.put(marketJSON);
        OkHttpClient marketHttpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(marketHttpClient);
        OkHttpClient calculationHttpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        CalculationExecutor calculationExecutor = new WhatToMineCalculationExecutor(calculationHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, marketExecutor, calculationExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (CalculationExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
