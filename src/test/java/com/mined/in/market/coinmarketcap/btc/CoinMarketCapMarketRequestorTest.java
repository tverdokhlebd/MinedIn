package com.mined.in.market.coinmarketcap.btc;

import static com.mined.in.coin.CoinType.BTC;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.MarketRequestorFactory;

import okhttp3.OkHttpClient;

/**
 * Tests of CoinMarketCap bitcoin requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class CoinMarketCapMarketRequestorTest {

    @Test
    public void testCorrectJsonResponse() throws MarketRequestorException {
        BigDecimal coinPrice = new BigDecimal("8923.57");
        JSONArray marketArray = new JSONArray();
        marketArray.put(new JSONObject("{ \"id\": \"bitcoin\", \"name\": \"Bitcoin\", \"symbol\": \"BTC\", \"rank\": \"1\", \"price_usd\": "
                + "\"8923.57\", \"price_btc\": \"1.0\", \"24h_volume_usd\": \"6369130000.0\", \"market_cap_usd\": \"151639340456\", "
                + "\"available_supply\": \"16993125.0\", \"total_supply\": \"16993125.0\", \"max_supply\": \"21000000.0\", "
                + "\"percent_change_1h\": \"-0.27\", \"percent_change_24h\": \"1.47\", \"percent_change_7d\": \"7.61\", \"last_updated\": "
                + "\"1524417569\" }"));
        OkHttpClient httpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP, httpClient);
        CoinMarket coinMarket = marketRequestor.requestBitcoinCoin();
        assertEquals(BTC, coinMarket.getCoin());
        assertEquals(coinPrice, coinMarket.getPrice());
    }

    @Test(expected = MarketRequestorException.class)
    public void testEmptyResponse() throws MarketRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP, httpClient);
        try {
            marketRequestor.requestBitcoinCoin();
        } catch (MarketRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = MarketRequestorException.class)
    public void test500HttpError() throws MarketRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP, httpClient);
        try {
            marketRequestor.requestBitcoinCoin();
        } catch (MarketRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
