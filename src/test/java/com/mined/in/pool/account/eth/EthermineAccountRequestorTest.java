package com.mined.in.pool.account.eth;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.Utils;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.ethermine.EthermineAccountRequestor;

import okhttp3.OkHttpClient;

/**
 * Tests of Ethermine ETH requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class EthermineAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0x95dd0a3165347c27f06378d290a2bd30cc667191";

    @Test
    public void testCorrectJsonResponse() throws AccountRequestorException {
        JSONObject responseStats = new JSONObject("{\"status\":\"OK\",\"data\":{\"time\":1521925200,\"lastSeen\":1521925179,"
                + "\"reportedHashrate\":543427584,\"currentHashrate\":492944444.4444444,\"validShares\":430,\"invalidShares\":0,"
                + "\"staleShares\":21,\"averageHashrate\":516185057.4712644,\"activeWorkers\":3,\"unpaid\":44018085413038540,"
                + "\"unconfirmed\":null,\"coinsPerMin\":0.000026465384193725707,\"usdPerMin\":0.014274898926411771,"
                + "\"btcPerMin\":0.0000015963919745655347}}");
        OkHttpClient httpClient = Utils.getHttpClient(responseStats.toString(), 200);
        AccountRequestor accountRequestor = new EthermineAccountRequestor(httpClient);
        Account account = accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.04401808541303854), account.getWalletBalance());
        assertEquals(BigDecimal.valueOf(543.427584), account.getTotalHashrate());
    }

    @Test(expected = AccountRequestorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{\"status\":\"ERROR\",\"error\":\"Invalid address\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountRequestor accountRequestor = new EthermineAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount(WALLET_ADDRESS);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Invalid address", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountRequestorException.class)
    public void test500HttpError() throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = new EthermineAccountRequestor(httpClient);
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
        AccountRequestor accountRequestor = new EthermineAccountRequestor(httpClient);
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
        AccountRequestor accountRequestor = new EthermineAccountRequestor(httpClient);
        try {
            accountRequestor.requestEthereumAccount("");
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
