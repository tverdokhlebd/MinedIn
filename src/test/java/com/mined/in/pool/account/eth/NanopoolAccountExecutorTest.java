package com.mined.in.pool.account.eth;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.nanopool.NanopoolAccountExecutor;

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

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException {
        JSONObject responseBalance = new JSONObject("{\"status\":true,\"data\":0.05288338}");
        JSONObject responseWorkers =
                new JSONObject("{ \"status\": true, \"data\": [ { \"worker\": \"tester\", \"hashrate\": 131.927 }, "
                        + "{ \"worker\": \"tester2\", \"hashrate\": 131.927 } ] }");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = null;
            if (request.url().encodedPath().indexOf("balance") != -1) {
                body = ResponseBody.create(MEDIA_JSON, responseBalance.toString());
            } else {
                body = ResponseBody.create(MEDIA_JSON, responseWorkers.toString());
            }
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        Account account = accountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.05288338), account.getWalletBalance());
        assertEquals(263.854, account.getTotalHashrate(), 0);
        assertEquals(2, account.getWorkerList().size());
        assertEquals("tester", account.getWorkerList().get(0).getName());
        assertEquals(131.927, account.getWorkerList().get(0).getHashrate(), 0);
        assertEquals("tester2", account.getWorkerList().get(1).getName());
        assertEquals(131.927, account.getWorkerList().get(1).getHashrate(), 0);
    }

    @Test(expected = AccountExecutorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountExecutorException {
        JSONObject responseJSON = new JSONObject("{\"status\":false,\"error\":\"Account not found\"}");
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON.toString(), 200);
        AccountExecutor accountExecutor = new NanopoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Account not found", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void test500HttpError() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
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
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
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
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
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
