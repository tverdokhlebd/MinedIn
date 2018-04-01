package com.mined.in.pool.account.eth;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.nanopool.NanopoolAccountRequestor;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of Nanopool ETH requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class NanopoolAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0x1bd02ad76aa949a452e3b14d002de68a7713f03b";
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject responseBalance = new JSONObject("{\"status\":true,\"data\":0.05288338}");
        JSONObject responseReportedHashrate =
                new JSONObject("{ \"status\": true, \"data\": 1711.978 }");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = null;
            if (request.url().encodedPath().indexOf("balance") != -1) {
                body = ResponseBody.create(MEDIA_JSON, responseBalance.toString());
            } else {
                body = ResponseBody.create(MEDIA_JSON, responseReportedHashrate.toString());
            }
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountRequestor accountRequestor = new NanopoolAccountRequestor(httpClient);
        Account account = accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.05288338), account.getWalletBalance());
        assertEquals(BigDecimal.valueOf(1711.978), account.getTotalHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject responseJSON = new JSONObject("{\"status\":false,\"error\":\"Account not found\"}");
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON.toString(), 200);
        AccountRequestor accountRequestor = new NanopoolAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Account not found", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = new NanopoolAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = new NanopoolAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testWithEmptyWalletAddress() throws AccountRequestorException {
        String errorCode = "BAD_WALLET";
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = new NanopoolAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
