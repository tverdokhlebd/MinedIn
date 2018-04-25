package com.mined.in.pool.account.xmr;

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
 * Tests of Dwarfpool monero requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class DwarfpoolAccountRequestorTest {

    private final static String WALLET_ADDRESS =
            "4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE1B";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject response =
                new JSONObject("{ \"autopayout_from\": \"2.000\", \"earning_24_hours\": \"0.00198981\", \"error\": false, \"immature_earning\": "
                        + "0.00018550505, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                        + "\"Wed, 25 Apr 2018 18:17:00 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 70.0, "
                        + "\"total_hashrate_calculated\": 0.42, \"transferring_to_balance\": 0, \"wallet\": "
                        + "\"0x4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE1B\", "
                        + "\"wallet_balance\": \"0.00878743\", \"workers\": {} }");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        Account account = accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.00878743), account.getWalletBalance());
        assertEquals(new BigDecimal("7E+7"), account.getReportedHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(DWARFPOOL, httpClient, false);
        try {
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
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
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
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
            accountRequestor.requestMoneroAccount(WALLET_ADDRESS);
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
            accountRequestor.requestMoneroAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("BAD_WALLET", e.getMessage());
            throw e;
        }
    }

}
