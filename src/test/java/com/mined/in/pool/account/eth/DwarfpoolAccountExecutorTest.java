package com.mined.in.pool.account.eth;

import static com.mined.in.error.ErrorCode.API_ERROR;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountExecutor;
import com.mined.in.pool.AccountExecutorException;
import com.mined.in.pool.dwarfpool.DwarfpoolAccountExecutor;

import okhttp3.OkHttpClient;

/**
 * Tests of Dwarfpool ethereum executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class DwarfpoolAccountExecutorTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException {
        JSONObject response = new JSONObject("{ \"autopayout_from\": \"5.000\", \"earning_24_hours\": \"0.01137842\", \"error\": false, "
                + "\"immature_earning\": 0.000455540976, \"last_payment_amount\": 0, \"last_payment_date\": null, \"last_share_date\": "
                + "\"Sat, 24 Mar 2018 21:09:25 GMT\", \"payout_daily\": false, \"payout_request\": false, \"total_hashrate\": 174.03, "
                + "\"total_hashrate_calculated\": 197.03, \"transferring_to_balance\": 0, \"wallet\": "
                + "\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\", \"wallet_balance\": \"0.78665394\", \"workers\": { \"dmtry\": "
                + "{ \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                + "\"last_submit\": \"Sat, 24 Mar 2018 21:09:32 GMT\", \"second_since_submit\": 342, \"worker\": \"dmtry\" }, "
                + "\"tv\": { \"alive\": true, \"hashrate\": 87.015, \"hashrate_below_threshold\": false, \"hashrate_calculated\": 98.515, "
                + "\"last_submit\": \"Sat, 24 Mar 2018 20:06:25 GMT\", \"second_since_submit\": 305, \"worker\": \"tv\" } } }");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(httpClient);
        Account account = accountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.78665394), account.getWalletBalance());
        assertEquals(BigDecimal.valueOf(174.03), account.getTotalHashrate());
    }

    @Test(expected = AccountExecutorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountExecutorException {
        JSONObject response = new JSONObject("{\"error\": true, \"error_code\": \"API_DOWN\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("API_DOWN", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void test500HttpError() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(httpClient);
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
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void testWithEmptyWalletAddress() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountExecutor accountExecutor = new DwarfpoolAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount("");
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("BAD_WALLET", e.getMessage());
            throw e;
        }
    }

}
