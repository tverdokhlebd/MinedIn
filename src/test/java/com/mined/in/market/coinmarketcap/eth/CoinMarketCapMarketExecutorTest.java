package com.mined.in.market.coinmarketcap;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.market.Market;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;

import okhttp3.OkHttpClient;

/**
 * Tests of CoinMarketCap executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class CoinMarketCapMarketExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws MarketExecutorException {
        BigDecimal coinPrice = new BigDecimal("536.854");
        JSONArray marketArray = new JSONArray();
        marketArray.put(new JSONObject("{ \"id\": \"bitcoin\", \"name\": \"Bitcoin\", \"symbol\": \"BTC\", \"rank\": \"1\", \"price_usd\": "
                + "\"8692.36\", \"price_btc\": \"1.0\", \"24h_volume_usd\": \"5574420000.0\", \"market_cap_usd\": \"147194903077\", "
                + "\"available_supply\": \"16933825.0\", \"total_supply\": \"16933825.0\", \"max_supply\": \"21000000.0\", "
                + "\"percent_change_1h\": \"0.51\", \"percent_change_24h\": \"-2.56\", \"percent_change_7d\": \"5.26\", \"last_updated\": "
                + "\"1521743668\" }"));
        marketArray.put(new JSONObject("{ \"id\": \"ethereum\", \"name\": \"Ethereum\", \"symbol\": \"ETH\", \"rank\": \"2\", \"price_usd\": "
                + "\"536.854\", \"price_btc\": \"0.0619693\", \"24h_volume_usd\": \"1560240000.0\", \"market_cap_usd\": "
                + "\"52799438836.0\", \"available_supply\": \"98349717.0\", \"total_supply\": \"98349717.0\", \"max_supply\": null, "
                + "\"percent_change_1h\": \"0.94\", \"percent_change_24h\": \"-4.19\", \"percent_change_7d\": \"-11.61\", "
                + "\"last_updated\": \"1521743654\" }"));
        OkHttpClient httpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(httpClient);
        Market market = marketExecutor.getMarket();
        assertEquals(coinPrice, market.getEthPrice());
    }

    @Test(expected = MarketExecutorException.class)
    public void testEmptyResponse() throws MarketExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(httpClient);
        try {
            marketExecutor.getMarket();
        } catch (MarketExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = MarketExecutorException.class)
    public void test500HttpError() throws MarketExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        MarketExecutor marketExecutor = new CoinMarketCapMarketExecutor(httpClient);
        try {
            marketExecutor.getMarket();
        } catch (MarketExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
