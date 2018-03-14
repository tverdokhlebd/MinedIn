package com.mined.in.exchanger.pair.exmo;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.pool.account.Utils;

import net.minidev.json.JSONObject;
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
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        JSONObject ethUsdJson = new JSONObject();
        ethUsdJson.put("buy_price", buyPrice.toString());
        ethUsdJson.put("sell_price", sellPrice.toString());
        JSONObject exchangerJSON = new JSONObject();
        exchangerJSON.put(pairName, ethUsdJson);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(exchangerJSON, 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testIncorrectJsonStructure() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        JSONObject ethUsdJson = new JSONObject();
        ethUsdJson.put("buyprice", buyPrice.toString());
        ethUsdJson.put("sellprice", sellPrice.toString());
        JSONObject exchangerJSON = new JSONObject();
        exchangerJSON.put(pairName, ethUsdJson);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(exchangerJSON, 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
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
