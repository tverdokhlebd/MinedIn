package com.mined.in.worker;

import static com.mined.in.coin.Coin.ETH;
import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.dwarfpool.DwarfpoolAccountExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of ETH mined worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ETHMinedWorkerTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException, PairExecutorException {
        BigDecimal walletBalance = BigDecimal.valueOf(0.78665394);
        JSONObject poolResponseJSON = new JSONObject("{\"error\": false, \"wallet_balance\": \"0.78665394\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        BigDecimal buyPrice = BigDecimal.valueOf(611.0803);
        BigDecimal sellPrice = BigDecimal.valueOf(615.99987);
        JSONObject exchangerJSON =
                new JSONObject("{\"ETH_USD\":{\"buy_price\":\"611.0803\",\"sell_price\":\"615.99987\",\"last_trade\":\"614.69\","
                        + "\"high\":\"657.555\",\"low\":\"603.128418\",\"avg\":\"626.85027354\",\"vol\":\"6578.09576192\","
                        + "\"vol_curr\":\"4043489.68389892\",\"updated\":1521147101}}");
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(exchangerJSON, 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, pairExecutor);
        MinedResult result = worker.calculate(WALLET_ADDRESS);
        assertEquals(walletBalance, result.getCoinBalance());
        assertEquals(walletBalance.multiply(buyPrice), result.getUsdBalance());
        assertEquals(buyPrice, result.getBuyPrice());
        assertEquals(sellPrice, result.getSellPrice());
    }

    @Test(expected = AccountExecutorException.class)
    public void testPoolError() throws AccountExecutorException, PairExecutorException {
        JSONObject poolResponseJSON = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, pairExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("API_DOWN", e.getMessage());
            throw e;
        }
    }

    @Test(expected = PairExecutorException.class)
    public void testExchangerError() throws AccountExecutorException, PairExecutorException {
        JSONObject poolResponseJSON = new JSONObject("{\"error\": false, \"wallet_balance\": \"0.78665394\"}");
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(new JSONObject(), 500);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        MinedWorker worker = MinedWorkerFactory.getMinedWorker(ETH, accountExecutor, pairExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
