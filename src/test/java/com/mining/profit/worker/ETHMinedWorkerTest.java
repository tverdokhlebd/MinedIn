package com.mining.profit.worker;

import static com.mining.profit.worker.MinedWorkerFactory.MinedWorkerType.ETH;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.exchanger.currencypair.exmo.ExmoCurrencyPairExecutor;
import com.mining.profit.pool.account.AccountExecutor;
import com.mining.profit.pool.account.dwarfpool.DwarfpoolAccountExecutor;

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
    public void calculateWithCorrectResponse() throws MinedWorkerException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
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
        assertEquals(buyPrice, result.getUsdRate());
    }

    @Test(expected = MinedWorkerException.class)
    public void calculateWithIncorrectResponseInAccountExecutor() throws MinedWorkerException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("wallet", WALLET_ADDRESS);
            bodyJSON.put("wallet_balance", walletBalance.doubleValue());
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
        worker.calculate(WALLET_ADDRESS);
    }

    @Test(expected = MinedWorkerException.class)
    public void calculateWithIncorrectResponseInCurrencyPairExecutor() throws MinedWorkerException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
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
            ethUSDJson.put("buy_price", buyPrice.longValue());
            ethUSDJson.put("sell_price", sellPrice.longValue());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put(currencyPairName, ethUSDJson);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient pairHttpClient = new OkHttpClient.Builder().addInterceptor(pairJSONInterceptor).build();
        CurrencyPairExecutor currencyPairExecutor = new ExmoCurrencyPairExecutor(pairHttpClient);
        // MinedWorker
        MinedWorker worker = MinedWorkerFactory.getAccountExecutor(ETH, accountExecutor, currencyPairExecutor);
        worker.calculate(WALLET_ADDRESS);
    }

    @Test(expected = MinedWorkerException.class)
    public void calculateWith500Error() throws MinedWorkerException {
        // AccountExecutor
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor accountJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("wallet", WALLET_ADDRESS);
            bodyJSON.put("wallet_balance", walletBalance.toString());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
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
        worker.calculate(WALLET_ADDRESS);
    }

}
