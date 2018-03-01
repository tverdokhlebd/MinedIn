package com.mined.in.worker;

import static com.mined.in.coin.Coin.ETH;
import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.exchanger.currencypair.CurrencyPairExecutor;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutorException;
import com.mined.in.exchanger.currencypair.exmo.ExmoCurrencyPairExecutor;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.dwarfpool.DwarfpoolAccountExecutor;

import net.minidev.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of ETH mined worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ETHMinedWorkerTest {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private static final String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void calculateWithCorrectResponse() throws AccountExecutorException, CurrencyPairExecutorException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("error", false);
            bodyJSON.put("wallet", WALLET_ADDRESS);
            bodyJSON.put("wallet_balance", walletBalance.toString());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient accountHttpClient = new OkHttpClient.Builder().addInterceptor(accountJSONInterceptor).build();
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        // CurrencyPairExecutor
        String currencyPairName = "ETH_USD";
        BigDecimal buyPrice = new BigDecimal("700");
        BigDecimal sellPrice = new BigDecimal("702");
        Interceptor pairJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject ethUSDJson = new JSONObject();
            ethUSDJson.put("buy_price", buyPrice.toString());
            ethUSDJson.put("sell_price", sellPrice.toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(currencyPairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient pairHttpClient = new OkHttpClient.Builder().addInterceptor(pairJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(pairHttpClient);
        // MinedWorker
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, currencyPairExecutor);
        MinedResult result = worker.calculate(WALLET_ADDRESS);
        assertEquals(walletBalance, result.getCoinsBalance());
        assertEquals(walletBalance.multiply(buyPrice), result.getUsdBalance());
        assertEquals(buyPrice, result.getBuyPrice());
        assertEquals(sellPrice, result.getSellPrice());
    }

    @Test(expected = AccountExecutorException.class)
    public void calculateWithAPIErrorInAccountExecutor() throws AccountExecutorException, CurrencyPairExecutorException {
        // AccountExecutor
        String errorCode = "API_DOWN";
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("error", true);
            bodyJSON.put("error_code", errorCode);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient accountHttpClient = new OkHttpClient.Builder().addInterceptor(accountJSONInterceptor).build();
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        // MinedWorker
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, null);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

    @Test(expected = CurrencyPairExecutorException.class)
    public void calculateWithHTTPErrorInCurrencyPairExecutor() throws AccountExecutorException, CurrencyPairExecutorException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("error", false);
            bodyJSON.put("wallet", WALLET_ADDRESS);
            bodyJSON.put("wallet_balance", walletBalance.toString());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient accountHttpClient = new OkHttpClient.Builder().addInterceptor(accountJSONInterceptor).build();
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(accountHttpClient);
        // CurrencyPairExecutor
        Interceptor pairJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
        };
        OkHttpClient pairHttpClient = new OkHttpClient.Builder().addInterceptor(pairJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(pairHttpClient);
        // MinedWorker
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, currencyPairExecutor);
        try {
            worker.calculate(WALLET_ADDRESS);
        } catch (CurrencyPairExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
