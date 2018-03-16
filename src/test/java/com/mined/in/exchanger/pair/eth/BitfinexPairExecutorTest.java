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
import com.mined.in.exchanger.pair.bitfinex.BitfinexPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Bitfinex executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class BitfinexPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("604.35");
        BigDecimal sellPrice = new BigDecimal("605.41");
        JSONObject response = new JSONObject(
                "{\"mid\":\"604.88\",\"bid\":\"604.35\",\"ask\":\"605.41\",\"last_price\":\"605.4\",\"low\":\"568.29\",\"high\":\"620.0\","
                        + "\"volume\":\"263124.01121006\",\"timestamp\":\"1521147391.4249253\"}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new BitfinexPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new BitfinexPairExecutor(httpClient);
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
        PairExecutor pairExecutor = new BitfinexPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
