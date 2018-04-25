package com.mined.in.pool.account.xmr;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.pool.PoolType.NANOPOOL;
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
import com.mined.in.pool.AccountRequestorFactory;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of Nanopool monero requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class NanopoolAccountRequestorTest {

    private final static String WALLET_ADDRESS =
            "48YQFWyu8mUFYHLvqvXvHF4t8YuGrjZs7JSSjrzb6onmWEm8BiP2mWBCrtf81Dsn3yChLipdyDWkejFS9ogKm9ug8ATPYek";
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject responseBalance = new JSONObject("{\"status\":true,\"data\":0.607640864350253}");
        JSONObject responseReportedHashrate =
                new JSONObject("{ \"status\": true, \"data\": 836430 }");
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
        AccountRequestor accountRequestor = AccountRequestorFactory.create(NANOPOOL, httpClient, false);
        Account account = accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.607640864350253), account.getWalletBalance());
        assertEquals(new BigDecimal("8.3643e+5"), account.getReportedHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject responseJSON = new JSONObject("{\"status\":false,\"error\":\"Account not found\"}");
        OkHttpClient httpClient = Utils.getHttpClient(responseJSON.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(NANOPOOL, httpClient, false);
        try {
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Account not found", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(NANOPOOL, httpClient, false);
        try {
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(NANOPOOL, httpClient, false);
        try {
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testWithEmptyWalletAddress() throws AccountRequestorException {
        String errorCode = "BAD_WALLET";
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(NANOPOOL, httpClient, false);
        try {
            accountRequestor.requestMoneroAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
