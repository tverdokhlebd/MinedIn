package com.mined.in.worker;

import static com.mined.in.coin.Coin.ETH;
import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.exchanger.pair.exmo.ExmoPairExecutor;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.dwarfpool.DwarfpoolAccountExecutor;

import org.json.JSONObject;
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
    private final static BigDecimal WALLET_BALANCE = new BigDecimal("0.12345678");

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException, PairExecutorException {
        JSONObject poolResponseJSON = new JSONObject();
        poolResponseJSON.put("error", false);
        poolResponseJSON.put("wallet_balance", WALLET_BALANCE.toString());
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
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
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, pairExecutor);
        MinedResult result = worker.calculate(WALLET_ADDRESS);
        assertEquals(WALLET_BALANCE, result.getCoinsBalance());
        assertEquals(WALLET_BALANCE.multiply(buyPrice), result.getUsdBalance());
        assertEquals(buyPrice, result.getBuyPrice());
        assertEquals(sellPrice, result.getSellPrice());
    }

    @Test(expected = AccountExecutorException.class)
    public void testPoolError() throws AccountExecutorException, PairExecutorException {
        String errorCode = "API_DOWN";
        JSONObject poolResponseJSON = new JSONObject();
        poolResponseJSON.put("error", true);
        poolResponseJSON.put("error_code", errorCode);
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(new JSONObject(), 200);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, pairExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

    @Test(expected = PairExecutorException.class)
    public void testExchangerError() throws AccountExecutorException, PairExecutorException {
        JSONObject poolResponseJSON = new JSONObject();
        poolResponseJSON.put("error", false);
        poolResponseJSON.put("wallet_balance", WALLET_BALANCE.toString());
        OkHttpClient accountHttpClient = Utils.getHttpClient(poolResponseJSON, 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        OkHttpClient exchangerHttpClient = Utils.getHttpClient(new JSONObject(), 500);
        PairExecutor pairExecutor = new ExmoPairExecutor(exchangerHttpClient);
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, pairExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (PairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
