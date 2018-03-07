package com.mined.in.pool.account;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.pool.account.nanopool.NanopoolAccountExecutor;

import net.minidev.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of Nanopool executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class NanopoolAccountExecutorTest {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private static final String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
    private static final String API_URL_WITH_WALLET_ADDRESS = "https://api.nanopool.org/v1/eth/balance/" + WALLET_ADDRESS;

    @Test
    public void getETHAccountWithCorrectResponse() throws AccountExecutorException {
        BigDecimal walletBalance = new BigDecimal("8.78392416842787");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            assertEquals(API_URL_WITH_WALLET_ADDRESS, request.url().url().toString());
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("status", true);
            bodyJSON.put("data", walletBalance.doubleValue());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        Account account = accountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(walletBalance, account.getWalletBalance());
    }

    @Test(expected = AccountExecutorException.class)
    public void getETHAccountWithIncorrectJSON() throws AccountExecutorException {
        BigDecimal walletBalance = new BigDecimal("8.78392416842787");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("statu", true);
            bodyJSON.put("dat", walletBalance.doubleValue());
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void getETHAccountWithAPIError() throws AccountExecutorException {
        String errorCode = "Account not found";
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("status", false);
            bodyJSON.put("error", errorCode);
            ResponseBody body = ResponseBody.create(MEDIA_JSON, bodyJSON.toJSONString());
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void getETHAccountWith500Error() throws AccountExecutorException {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, "");
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(500).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

}
