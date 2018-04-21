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
        BigDecimal coinPrice = new BigDecimal("231.502");
        JSONArray marketArray = new JSONArray();
        marketArray.put(new JSONObject("{ \"id\": \"zcash\", \"name\": \"Zcash\", \"symbol\": \"ZEC\", \"rank\": \"26\", \"price_usd\": "
                + "\"231.502\", \"price_btc\": \"0.0278008\", \"24h_volume_usd\": \"50470000.0\", \"market_cap_usd\": \"858034672.0\", "
                + "\"available_supply\": \"3706381.0\", \"total_supply\": \"3706381.0\", \"max_supply\": null, \"percent_change_1h\": "
                + "\"0.15\", \"percent_change_24h\": \"3.91\", \"percent_change_7d\": \"26.52\", \"last_updated\": \"1523795950\" }"));
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
            marketRequestor.requestEthereumCoin();
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
            marketRequestor.requestEthereumCoin();
        } catch (MarketRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
