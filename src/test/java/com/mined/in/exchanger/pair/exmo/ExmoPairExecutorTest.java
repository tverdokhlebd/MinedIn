package com.mined.in.exchanger.pair.exmo;

import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.error.ErrorCode;
import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;

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
public class ExmoPairExecutorTest {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private static final String API_URL = "https://api.exmo.com/v1/ticker/";

    @Test
    public void getETHUSDPairWithCorrectResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL, request.url().url().toString());
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buy_price", buyPrice.toString());
            ethUSDJson.put("sell_price", sellPrice.toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(pairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PairExecutor pairExecutor = new ExmoPairExecutor(httpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void getETHUSDPairWithIncorrectJSON() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL, request.url().url().toString());
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buyprice", buyPrice.toString());
            ethUSDJson.put("sellprice", sellPrice.toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(pairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PairExecutor pairExecutor = new ExmoPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = PairExecutorException.class)
    public void getETHUSDPairWith500Error() throws PairExecutorException {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, "");
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PairExecutor pairExecutor = new ExmoPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(ErrorCode.HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
