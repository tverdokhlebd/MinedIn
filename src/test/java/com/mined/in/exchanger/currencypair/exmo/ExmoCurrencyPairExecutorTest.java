package com.mined.in.exchanger.currencypair.exmo;

import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.exchanger.currencypair.CurrencyPair;
import com.mined.in.exchanger.currencypair.CurrencyPairException;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutor;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutorException;

import net.minidev.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of Exmo executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ExmoCurrencyPairExecutorTest {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private static final String API_URL = "https://api.exmo.com/v1/ticker/";

    @Test
    public void getETHUSDPairWithCorrectResponse() throws CurrencyPairExecutorException, CurrencyPairException {
        String currencyPairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL, request.url().url().toString());
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buy_price", buyPrice.toString());
            ethUSDJson.put("sell_price", sellPrice.toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(currencyPairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(httpClient);
        CurrencyPair currencyPair = currencyPairExecutor.getETHUSDPair();
        assertEquals(currencyPairName, currencyPair.getPairName());
        assertEquals(buyPrice, currencyPair.getBuyPrice());
        assertEquals(sellPrice, currencyPair.getSellPrice());
    }

    @Test(expected = CurrencyPairException.class)
    public void getETHUSDPairWithIncorrectResponse() throws CurrencyPairExecutorException, CurrencyPairException {
        String currencyPairName = "ETH_USD";
        Long buyPrice = 700L;
        Long sellPrice = 702L;
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL, request.url().url().toString());
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buy_price", buyPrice);
            ethUSDJson.put("sell_price", sellPrice);
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(currencyPairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(httpClient);
        currencyPairExecutor.getETHUSDPair();
    }

    @Test(expected = CurrencyPairException.class)
    public void getETHUSDPairWithIncorrectJSON() throws CurrencyPairExecutorException, CurrencyPairException {
        String currencyPairName = "ETH_USD";
        Long buyPrice = 700L;
        Long sellPrice = 702L;
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL, request.url().url().toString());
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buyprice", buyPrice);
            ethUSDJson.put("sellprice", sellPrice);
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(currencyPairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(httpClient);
        currencyPairExecutor.getETHUSDPair();
    }

    @Test(expected = CurrencyPairExecutorException.class)
    public void getETHUSDPairWith500Error() throws CurrencyPairExecutorException, CurrencyPairException {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, "");
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(httpClient);
        currencyPairExecutor.getETHUSDPair();
    }

}
