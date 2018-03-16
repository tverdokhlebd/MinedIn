package com.mined.in.exchanger.pair.eth;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.exchanger.pair.gdax.GdaxPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Gdax executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class GdaxPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("604.96");
        BigDecimal sellPrice = new BigDecimal("604.97");
        JSONObject response = new JSONObject("{\"trade_id\":30637940,\"price\":\"604.97000000\",\"size\":\"1.02580000\","
                + "\"bid\":\"604.96\",\"ask\":\"604.97\",\"volume\":\"181789.34648913\",\"time\":\"2018-03-15T20:45:42.307000Z\"}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new GdaxPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new GdaxPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = PairExecutorException.class)
    public void test500HttpError() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 500);
        PairExecutor pairExecutor = new GdaxPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
