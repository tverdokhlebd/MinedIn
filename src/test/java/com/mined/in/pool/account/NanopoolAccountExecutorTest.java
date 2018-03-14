package com.mined.in.pool.account;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.pool.account.nanopool.NanopoolAccountExecutor;

import net.minidev.json.JSONObject;
import okhttp3.OkHttpClient;

/**
 * Tests of Nanopool executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class NanopoolAccountExecutorTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
    private final static BigDecimal WALLET_BALANCE = new BigDecimal("0.78392416842787");

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", true);
        responseJSON.put("data", WALLET_BALANCE.doubleValue());
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON, 200);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        Account account = accountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(WALLET_BALANCE, account.getWalletBalance());
    }

    @Test(expected = AccountExecutorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountExecutorException {
        String errorCode = "Account not found";
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", false);
        responseJSON.put("error", errorCode);
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON, 200);
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
    public void testIncorrectJsonStructure() throws AccountExecutorException {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status_", true);
        responseJSON.put("data ", WALLET_BALANCE.doubleValue());
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON, 200);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void test500HttpError() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 500);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void testEmptyResponse() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void testWithEmptyWalletAddress() throws AccountExecutorException {
        String errorCode = "BAD_WALLET";
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject(), 200);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount("");
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
