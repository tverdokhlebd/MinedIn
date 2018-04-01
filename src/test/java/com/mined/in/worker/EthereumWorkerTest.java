package com.mined.in.worker;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.coin.CoinInfo;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.coinmarketcap.CoinMarketCapMarketRequestor;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.dwarfpool.DwarfpoolAccountRequestor;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.reward.whattomine.WhatToMineRewardRequestor;

import okhttp3.OkHttpClient;

/**
 * Tests of worker for calculating ethereum earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class EthereumWorkerTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
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
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient);
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
        MarketRequestor marketRequestor = new CoinMarketCapMarketRequestor(marketHttpClient);
        JSONObject rewardResponse =
                new JSONObject("{\"id\":151,\"name\":\"Ethereum\",\"tag\":\"ETH\",\"algorithm\":\"Ethash\",\"block_time\":\"14.4406\","
                        + "\"block_reward\":2.91,\"block_reward24\":2.91000000000001,\"block_reward3\":2.91,\"block_reward7\":2.91,"
                        + "\"last_block\":5319532,\"difficulty\":3.23405110864068e+15,\"difficulty24\":3.28768043075892e+15,"
                        + "\"difficulty3\":3.26138538264012e+15,\"difficulty7\":3.26159879702061e+15,\"nethash\":223955452587889,"
                        + "\"exchange_rate\":0.061054,\"exchange_rate24\":0.0607868093126386,\"exchange_rate3\":0.0611831939414956,"
                        + "\"exchange_rate7\":0.062520153768632,\"exchange_rate_vol\":7094.59432922,\"exchange_rate_curr\":\"BTC\","
                        + "\"market_cap\":\"$51,261,485,391\",\"pool_fee\":\"0.000000\",\"estimated_rewards\":\"0.006424\","
                        + "\"btc_revenue\":\"0.00039220\",\"revenue\":\"$3.35\",\"cost\":\"$0.97\",\"profit\":\"$2.37\","
                        + "\"status\":\"Active\",\"lagging\":false,\"timestamp\":1521986783}");
        OkHttpClient rewardHttpClient = Utils.getHttpClient(rewardResponse.toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(rewardHttpClient);
        EarningsWorker worker = EarningsWorkerFactory.create(ETH, accountRequestor, marketRequestor, rewardRequestor);
        Earnings earnings = worker.calculate(WALLET_ADDRESS);
        assertEquals(walletBalance, earnings.getCoinBalance());
        assertEquals(walletBalance.multiply(coinPrice), earnings.getUsdBalance());
        assertEquals(coinPrice, earnings.getCoinPrice());
        CoinInfo coinInfo = earnings.getEstimatedReward().getCoinInfo();
        assertEquals(ETH, coinInfo.getCoinType());
        assertEquals(BigDecimal.valueOf(174), earnings.getEstimatedReward().getTotalHashrate());
        assertEquals(BigDecimal.valueOf(14.4406), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(2.91), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(5319532), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(3.23405110864068e+15), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(223955452587889L), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000554), earnings.getEstimatedReward().getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.013306), earnings.getEstimatedReward().getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.093142), earnings.getEstimatedReward().getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.39918), earnings.getEstimatedReward().getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(4.85669), earnings.getEstimatedReward().getRewardPerYear());
    }

    @Test(expected = AccountRequestorException.class)
    public void testPoolError() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        JSONObject poolResponseJSON = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient);
        OkHttpClient marketHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        MarketRequestor marketRequestor = new CoinMarketCapMarketRequestor(marketHttpClient);
        OkHttpClient rewardHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(rewardHttpClient);
        EarningsWorker worker = EarningsWorkerFactory.create(ETH, accountRequestor, marketRequestor, rewardRequestor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("API_DOWN", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MarketRequestorException.class)
    public void testMarketError() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
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
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient);
        OkHttpClient marketHttpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        MarketRequestor marketRequestor = new CoinMarketCapMarketRequestor(marketHttpClient);
        OkHttpClient rewardHttpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(rewardHttpClient);
        EarningsWorker worker = EarningsWorkerFactory.create(ETH, accountRequestor, marketRequestor, rewardRequestor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (MarketRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = RewardRequestorException.class)
    public void testRewardError() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
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
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient);
        JSONObject marketJSON =
                new JSONObject("{ \"id\": \"ethereum\", \"name\": \"Ethereum\", \"symbol\": \"ETH\", \"rank\": \"2\", \"price_usd\": \"536.854\", "
                        + "\"price_btc\": \"0.0619693\", \"24h_volume_usd\": \"1560240000.0\", \"market_cap_usd\": \"52799438836.0\", "
                        + "\"available_supply\": \"98349717.0\", \"total_supply\": \"98349717.0\", \"max_supply\": null, "
                        + "\"percent_change_1h\": \"0.94\", \"percent_change_24h\": \"-4.19\", \"percent_change_7d\": \"-11.61\", "
                        + "\"last_updated\": \"1521743654\" }");
        JSONArray marketArray = new JSONArray();
        marketArray.put(marketJSON);
        OkHttpClient marketHttpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketRequestor marketRequestor = new CoinMarketCapMarketRequestor(marketHttpClient);
        OkHttpClient rewardHttpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(rewardHttpClient);
        EarningsWorker worker = EarningsWorkerFactory.create(ETH, accountRequestor, marketRequestor, rewardRequestor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (RewardRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
