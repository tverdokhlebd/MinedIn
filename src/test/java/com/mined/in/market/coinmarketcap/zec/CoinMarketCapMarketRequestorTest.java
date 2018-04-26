package com.mined.in.market.coinmarketcap.zec;

import static com.mined.in.coin.CoinType.ZEC;
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
 * Tests of CoinMarketCap zcash requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class CoinMarketCapMarketRequestorTest {

    @Test
    public void testCorrectJsonResponse() throws MarketRequestorException {
        BigDecimal coinPrice = new BigDecimal("286.027");
        JSONArray marketArray = new JSONArray();
        marketArray.put(new JSONObject("{ \"id\": \"zcash\", \"name\": \"Zcash\", \"symbol\": \"ZEC\", \"rank\": \"24\", \"price_usd\": "
                + "\"286.027\", \"price_btc\": \"0.0322474\", \"24h_volume_usd\": \"67396000.0\", \"market_cap_usd\": \"1083193187.0\", "
                + "\"available_supply\": \"3787031.0\", \"total_supply\": \"3787031.0\", \"max_supply\": null, \"percent_change_1h\": "
                + "\"-0.18\", \"percent_change_24h\": \"0.77\", \"percent_change_7d\": \"11.41\", \"last_updated\": \"1524767950\" }"));
        OkHttpClient httpClient = Utils.getHttpClient(marketArray.toString(), 200);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP, httpClient);
        CoinMarket coinMarket = marketRequestor.requestZcashCoin();
        assertEquals(ZEC, coinMarket.getCoin());
        assertEquals(coinPrice, coinMarket.getPrice());
    }

    @Test(expected = MarketRequestorException.class)
    public void testEmptyResponse() throws MarketRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP, httpClient);
        try {
            marketRequestor.requestZcashCoin();
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
            marketRequestor.requestZcashCoin();
        } catch (MarketRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
