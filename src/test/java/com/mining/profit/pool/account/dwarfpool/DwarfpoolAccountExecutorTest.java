package com.mining.profit.pool.account.dwarfpool;

import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mining.profit.pool.account.PoolAccount;
import com.mining.profit.pool.account.PoolAccountException;
import com.mining.profit.pool.account.PoolAccountExecutor;
import com.mining.profit.pool.account.PoolAccountExecutorException;

import net.minidev.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SpringBootTest
public class DwarfpoolAccountExecutorTest {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private static final String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
    private static final String API_URL_WITH_WALLET_ADDRESS = "http://dwarfpool.com/eth/api?wallet=" + WALLET_ADDRESS;

    @Test
    public void getETHAccountWithCorrectResponse() throws PoolAccountExecutorException, PoolAccountException {
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL_WITH_WALLET_ADDRESS, request.url().url().toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("wallet", WALLET_ADDRESS);
            bodyJSON.put("wallet_balance", walletBalance.toString());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PoolAccountExecutor poolAccountExecutor = new DwarfpoolAccountExecutor(httpClient);
        PoolAccount poolAccount = poolAccountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, poolAccount.getWalletAddress());
        assertEquals(walletBalance, poolAccount.getWalletBalance());
    }

    @Test(expected = PoolAccountException.class)
    public void getETHAccountWithIncorrectResponse() throws PoolAccountExecutorException, PoolAccountException {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("wallet", 123456789);
            bodyJSON.put("wallet_balance", 123456789);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PoolAccountExecutor poolAccountExecutor = new DwarfpoolAccountExecutor(httpClient);
        poolAccountExecutor.getETHAccount(WALLET_ADDRESS);
    }

    @Test(expected = PoolAccountException.class)
    public void getETHAccountWithIncorrectJSON() throws PoolAccountExecutorException, PoolAccountException {
        BigDecimal walletBalance = new BigDecimal("0.45431668");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("walet", WALLET_ADDRESS);
            bodyJSON.put("walet_balance", walletBalance.toString());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PoolAccountExecutor poolAccountExecutor = new DwarfpoolAccountExecutor(httpClient);
        poolAccountExecutor.getETHAccount(WALLET_ADDRESS);
    }

    @Test(expected = PoolAccountExecutorException.class)
    public void getETHAccountWith500Error() throws PoolAccountExecutorException, PoolAccountException {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, "");
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        PoolAccountExecutor poolAccountExecutor = new DwarfpoolAccountExecutor(httpClient);
        poolAccountExecutor.getETHAccount(WALLET_ADDRESS);
    }

}
