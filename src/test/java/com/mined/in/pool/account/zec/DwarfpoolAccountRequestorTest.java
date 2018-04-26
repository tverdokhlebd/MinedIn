package com.mined.in.pool.account.zec;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.pool.PoolType.DWARFPOOL;
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

import okhttp3.OkHttpClient;

/**
 * Tests of Dwarfpool ethereum requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class DwarfpoolAccountRequestorTest {

    private final static String WALLET_ADDRESS = "t1Tyn5Xivt1oKRAPzrDYfTcPTptqpjQk3Cp";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject response =
                new JSONObject("{ \"autopayout_from\": \"0.010\", \"earning_24_hours\": \"?\", \"error\": false, \"immature_earning\": "
                        + "0, \"last_payment_amount\": \"1.61826725\", \"last_payment_date\": \"Mon, 23 Apr 2018 13:22:40 GMT\", "
                        + "\"last_share_date\": \"Thu, 26 Apr 2018 19:52:01 GMT\", \"payout_daily\": false, \"payout_request\": "
                        + "false, \"total_hashrate\": 281040.67, \"total_hashrate_calculated\": 281040.67, \"transferring_to_balance\": "
                        + "0, \"wallet\": \"0xt1Tyn5Xivt1oKRAPzrDYfTcPTptqpjQk3Cp\", \"wallet_balance\": \"0.00000001\", \"workers\": "
                        + "{ \"\": { \"alive\": true, \"hashrate\": 281040.67, \"hashrate_below_threshold\": false, \"hashrate_calculated\": "
                        + "281040.67, \"last_submit\": \"Thu, 26 Apr 2018 19:52:01 GMT\", \"second_since_submit\": 98, \"worker\": \"\" } } }");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        Account account = accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(new BigDecimal("1E-8"), account.getWalletBalance());
        assertEquals(new BigDecimal("2.8104067E+11"), account.getReportedHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        try {
            accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("API_DOWN", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        try {
            accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        try {
            accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testWithEmptyWalletAddress() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        try {
            accountRequestor.requestZcashAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("BAD_WALLET", e.getMessage());
            throw e;
        }
    }

}
