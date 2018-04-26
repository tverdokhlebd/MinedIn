package com.mined.in.pool.account.zec;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.pool.PoolType.ETHERMINE;
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
 * Tests of Ethermine ethereum requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class EthermineAccountRequestorTest {

    private final static String WALLET_ADDRESS = "t1NsPBYUS3yX2gUW5woef6xU5trRRvagtWh";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject responseStats =
                new JSONObject("{\"status\":\"OK\",\"data\":{\"time\":1524772200,\"lastSeen\":1524772273,\"reportedHashrate\":"
                        + "0,\"currentHashrate\":3626.666666666667,\"validShares\":136,\"invalidShares\":0,\"staleShares\":0,\"averageHashrate\":"
                        + "3256.1111111111113,\"activeWorkers\":2,\"unpaid\":2091163,\"unconfirmed\":767970,\"coinsPerMin\":0.000026970490151483,"
                        + "\"usdPerMin\":0.007718145166649891,\"btcPerMin\":8.679103730747229e-7}}");
        OkHttpClient httpClient = Utils.getHttpClient(responseStats.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        Account account = accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.000000000002091163), account.getWalletBalance());
        assertEquals(BigDecimal.valueOf(0), account.getReportedHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"status\":\"ERROR\",\"error\":\"Invalid address\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Invalid address", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
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
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestZcashAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(JSON_ERROR, e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testWithEmptyWalletAddress() throws AccountRequestorException {
        String errorCode = "BAD_WALLET";
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestZcashAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
