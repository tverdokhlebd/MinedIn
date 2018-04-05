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
    private final static JSONObject POOL_RESPONSE =
            new JSONObject("{ \"autopayout_from\": \"5.000\", \"earning_24_hours\": \"0.01137842\", \"error\": false, "
                    + "\"immature_earning\": 0.000455540976, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                    + "\"Sat, 24 Mar 2018 21:09:25 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 174, "
                    + "\"total_hashrate_calculated\": 197.03, \"transferring_to_balance\": 0, \"wallet\": "
                    + "\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\", \"wallet_balance\": \"0.78665394\", \"workers\": { \"dmtry\": "
                    + "{ \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                    + "\"last_submit\": \"Sat, 24 Mar 2018 21:09:32 GMT\", \"second_since_submit\": 342, \"worker\": \"dmtry\" }, "
                    + "\"tv\": { \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                    + "\"last_submit\": \"Sat, 24 Mar 2018 20:06:25 GMT\", \"second_since_submit\": 305, \"worker\": \"tv\" } } }");
    private final static JSONArray MARKET_RESPONSE =
            new JSONArray("[{ \"id\": \"ethereum\", \"name\": \"Ethereum\", \"symbol\": \"ETH\", \"rank\": \"2\", \"price_usd\": \"536.854\", "
                    + "\"price_btc\": \"0.0619693\", \"24h_volume_usd\": \"1560240000.0\", \"market_cap_usd\": \"52799438836.0\", "
                    + "\"available_supply\": \"98349717.0\", \"total_supply\": \"98349717.0\", \"max_supply\": null, "
                    + "\"percent_change_1h\": \"0.94\", \"percent_change_24h\": \"-4.19\", \"percent_change_7d\": \"-11.61\", "
                    + "\"last_updated\": \"1521743654\" }]");
    private final static JSONObject REWARD_RESPONSE =
            new JSONObject("{\"id\":151,\"name\":\"Ethereum\",\"tag\":\"ETH\",\"algorithm\":\"Ethash\",\"block_time\":\"14.5339\","
                    + "\"block_reward\":2.91,\"block_reward24\":2.91000000000001,\"block_reward3\":2.91,\"block_reward7\":2.91000000000001,"
                    + "\"last_block\":5386367,\"difficulty\":3.08359377889012e+15,\"difficulty24\":3.15113330321897e+15,\"difficulty3\""
                    + ":3.15333133159424e+15,\"difficulty7\":3.18760003087741e+15,\"nethash\":212165611356216,\"exchange_rate\":0.055872,"
                    + "\"exchange_rate24\":0.0558127599164927,\"exchange_rate3\":0.0551308601768426,\"exchange_rate7\":0.0556511829329325,"
                    + "\"exchange_rate_vol\":8433.85786587,\"exchange_rate_curr\":\"BTC\",\"market_cap\":\"$36,926,348,303\",\"pool_fee\":"
                    + "\"0.000000\",\"estimated_rewards\":\"0.013883\",\"btc_revenue\":\"0.00077568\",\"revenue\":\"$5.20\",\"cost\":"
                    + "\"$0.97\",\"profit\":\"$4.23\",\"status\":\"Active\",\"lagging\":false,\"timestamp\":1522951041}");

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        BigDecimal walletBalance = BigDecimal.valueOf(0.78665394);
        OkHttpClient accountHttpClient = Utils.getHttpClient(POOL_RESPONSE.toString(), 200);
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient, false);
        BigDecimal coinPrice = BigDecimal.valueOf(536.854);
        OkHttpClient marketHttpClient = Utils.getHttpClient(MARKET_RESPONSE.toString(), 200);
        MarketRequestor marketRequestor = new CoinMarketCapMarketRequestor(marketHttpClient);
        OkHttpClient rewardHttpClient = Utils.getHttpClient(REWARD_RESPONSE.toString(), 200);
        RewardRequestor rewardRequestor = new WhatToMineRewardRequestor(rewardHttpClient);
        EarningsWorker worker = EarningsWorkerFactory.create(ETH, accountRequestor, marketRequestor, rewardRequestor);
        Earnings earnings = worker.calculate(WALLET_ADDRESS);
        assertEquals(walletBalance, earnings.getCoinBalance());
        assertEquals(walletBalance.multiply(coinPrice), earnings.getUsdBalance());
        assertEquals(coinPrice, earnings.getCoinPrice());
        CoinInfo coinInfo = earnings.getEstimatedReward().getCoinInfo();
        assertEquals(ETH, coinInfo.getCoinType());
        assertEquals(BigDecimal.valueOf(1.74e+8), earnings.getEstimatedReward().getReportedHashrate());
        assertEquals(BigDecimal.valueOf(14.5339), coinInfo.getBlockTime());
        assertEquals(BigDecimal.valueOf(2.91), coinInfo.getBlockReward());
        assertEquals(BigDecimal.valueOf(5386367), coinInfo.getBlockCount());
        assertEquals(BigDecimal.valueOf(3.08359377889012e+15), coinInfo.getDifficulty());
        assertEquals(BigDecimal.valueOf(212165611356216L), coinInfo.getNetworkHashrate());
        assertEquals(BigDecimal.valueOf(0.000578), earnings.getEstimatedReward().getRewardPerHour());
        assertEquals(BigDecimal.valueOf(0.013883), earnings.getEstimatedReward().getRewardPerDay());
        assertEquals(BigDecimal.valueOf(0.097181), earnings.getEstimatedReward().getRewardPerWeek());
        assertEquals(BigDecimal.valueOf(0.41649), earnings.getEstimatedReward().getRewardPerMonth());
        assertEquals(BigDecimal.valueOf(5.067295), earnings.getEstimatedReward().getRewardPerYear());
    }

    @Test(expected = AccountRequestorException.class)
    public void testPoolError() throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        JSONObject poolResponseJSON = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON.toString(), 200);
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient, false);
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
        OkHttpClient accountHttpClient = Utils.getHttpClient(POOL_RESPONSE.toString(), 200);
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient, false);
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
        OkHttpClient accountHttpClient = Utils.getHttpClient(POOL_RESPONSE.toString(), 200);
        AccountRequestor accountRequestor = new DwarfpoolAccountRequestor(accountHttpClient, false);
        OkHttpClient marketHttpClient = Utils.getHttpClient(MARKET_RESPONSE.toString(), 200);
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
