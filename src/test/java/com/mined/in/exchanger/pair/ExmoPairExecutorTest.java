package com.mined.in.exchanger.pair;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Exmo executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ExmoPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("611.0803");
        BigDecimal sellPrice = new BigDecimal("615.99987");
        JSONObject response =
                new JSONObject("{\"ETH_USD\":{\"buy_price\":\"611.0803\",\"sell_price\":\"615.99987\",\"last_trade\":\"614.69\","
                        + "\"high\":\"657.555\",\"low\":\"603.128418\",\"avg\":\"626.85027354\",\"vol\":\"6578.09576192\","
                        + "\"vol_curr\":\"4043489.68389892\",\"updated\":1521147101}}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(httpClient);
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
        PairExecutor pairExecutor = new ExmoPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
