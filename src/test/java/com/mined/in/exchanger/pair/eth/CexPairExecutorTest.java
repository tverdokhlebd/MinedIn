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
import com.mined.in.exchanger.pair.cex.CexPairExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Cex executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class CexPairExecutorTest {

    @Test
    public void testCorrectJsonResponse() throws PairExecutorException {
        String pairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("616.14");
        BigDecimal sellPrice = new BigDecimal("618");
        JSONObject response = new JSONObject(
                "{\"timestamp\":\"1521147619\",\"low\":\"586.6\",\"high\":\"646\",\"last\":\"618\",\"volume\":\"4599.28553400\","
                        + "\"volume30d\":\"76255.57395700\",\"bid\":616.14,\"ask\":618}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(response, 200);
        PairExecutor pairExecutor = new CexPairExecutor(exchangerHttpClient);
        Pair pair = pairExecutor.getETHUSDPair();
        assertEquals(pairName, pair.getPairName());
        assertEquals(buyPrice, pair.getBuyPrice());
        assertEquals(sellPrice, pair.getSellPrice());
    }

    @Test(expected = PairExecutorException.class)
    public void testEmptyResponse() throws PairExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new CexPairExecutor(httpClient);
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
        PairExecutor pairExecutor = new CexPairExecutor(httpClient);
        try {
            pairExecutor.getETHUSDPair();
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
