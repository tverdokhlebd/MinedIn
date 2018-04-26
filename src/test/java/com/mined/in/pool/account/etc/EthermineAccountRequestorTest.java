package com.mined.in.pool.account.etc;

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

    private final static String WALLET_ADDRESS = "0x79cdc994c16f7cffaa161697786860ab4f1cd860";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject responseStats =
                new JSONObject("{\"status\":\"OK\",\"data\":{\"time\":1524771600,\"lastSeen\":1524771589,\"reportedHashrate\""
                        + ":515215627,\"currentHashrate\":424000000,\"validShares\":379,\"invalidShares\":0,\"staleShares\":4,"
                        + "\"averageHashrate\":491029320.98765427,\"activeWorkers\":3,\"unpaid\":837960709137198500,\"unconfirmed\":null,"
                        + "\"coinsPerMin\":0.0009176622943319528,\"usdPerMin\":0.018490895230788847,\"btcPerMin\":0.000002083093408133533}}");
        OkHttpClient httpClient = Utils.getHttpClient(responseStats.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        Account account = accountRequestor.requestEthereumClassicAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(new BigDecimal("0.8379607091371985"), account.getWalletBalance());
        assertEquals(BigDecimal.valueOf(515215627), account.getReportedHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"status\":\"ERROR\",\"error\":\"Invalid address\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestEthereumClassicAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Invalid address", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithNoDataError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"status\":\"OK\",\"data\":\"NO DATA\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestEthereumClassicAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("NO DATA", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(ETHERMINE, httpClient, false);
        try {
            accountRequestor.requestEthereumClassicAccount(WALLET_ADDRESS);
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
            accountRequestor.requestEthereumClassicAccount(WALLET_ADDRESS);
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
            accountRequestor.requestEthereumClassicAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
