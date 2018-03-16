package com.mined.in.exchanger.pair;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.exchanger.pair.bitstamp.BitstampPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Bitstamp executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class BitstampPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("603.51");
        BigDecimal sellPrice = new BigDecimal("603.82");
        JSONObject response = new JSONObject(
                "{\"high\": \"620.00\", \"last\": \"603.48\", \"timestamp\": \"1521147313\", \"bid\": \"603.51\", \"vwap\": \"601.37\", \"volume\": \"48896.93815440\", \"low\": \"571.56\", \"ask\": \"603.82\", \"open\": \"612.00\"}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new BitstampPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new BitstampPairExecutor(httpClient);
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
        PairExecutor pairExecutor = new BitstampPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
