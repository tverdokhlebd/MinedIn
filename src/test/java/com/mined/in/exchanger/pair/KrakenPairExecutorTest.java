package com.mined.in.exchanger.pair;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.exchanger.pair.kraken.KrakenPairExecutor;
import com.mined.in.pool.account.Utils;

import okhttp3.OkHttpClient;

/**
 * Tests of Kraken executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class KrakenPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("603.59");
        BigDecimal sellPrice = new BigDecimal("603.96");
        JSONObject response = new JSONObject(
                "{\"error\":[],\"result\":{\"XETHZUSD\":{\"a\":[\"603.96000\",\"1\",\"1.000\"],\"b\":[\"603.59000\",\"2\",\"2.000\"],"
                        + "\"c\":[\"603.95000\",\"1.92930000\"],\"v\":[\"61159.81065756\",\"80557.71035729\"],\"p\":[\"596.48144\",\"597.47690\"],"
                        + "\"t\":[17711,23132],\"l\":[\"570.00000\",\"570.00000\"],\"h\":[\"621.40000\",\"621.40000\"],\"o\":\"610.50000\"}}}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new KrakenPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new KrakenPairExecutor(httpClient);
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
        PairExecutor pairExecutor = new KrakenPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
