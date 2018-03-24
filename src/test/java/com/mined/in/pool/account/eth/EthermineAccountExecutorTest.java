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
import com.mined.in.pool.account.ethermine.EthermineAccountExecutor;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of Ethermine executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class EthermineAccountExecutorTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Test
    public void testCorrectJsonResponse() throws AccountExecutorException {
        JSONObject responseStats = new JSONObject("{\"status\":\"OK\",\"data\":{\"time\":1521925200,\"lastSeen\":1521925179,"
                + "\"reportedHashrate\":543427584,\"currentHashrate\":492944444.4444444,\"validShares\":430,\"invalidShares\":0,"
                + "\"staleShares\":21,\"averageHashrate\":516185057.4712644,\"activeWorkers\":3,\"unpaid\":44018085413038540,"
                + "\"unconfirmed\":null,\"coinsPerMin\":0.000026465384193725707,\"usdPerMin\":0.014274898926411771,"
                + "\"btcPerMin\":0.0000015963919745655347}}");
        JSONObject responseWorkers = new JSONObject("{\"status\":\"OK\",\"data\":[{\"worker\":\"sudarman1\",\"time\":1521925200,"
                + "\"lastSeen\":1521925166,\"reportedHashrate\":175752283,\"currentHashrate\":156500000,\"validShares\":135,"
                + "\"invalidShares\":0,\"staleShares\":9,\"averageHashrate\":164591324.20091322},{\"worker\":\"sudarman2\","
                + "\"time\":1521925200,\"lastSeen\":1521925166,\"reportedHashrate\":189536898,\"currentHashrate\":173222222.2222222,"
                + "\"validShares\":152,\"invalidShares\":0,\"staleShares\":6,\"averageHashrate\":183365740.74074072},"
                + "{\"worker\":\"sudarman3\",\"time\":1521925200,\"lastSeen\":1521925179,\"reportedHashrate\":178138403,"
                + "\"currentHashrate\":163222222.2222222,\"validShares\":143,\"invalidShares\":0,\"staleShares\":6,"
                + "\"averageHashrate\":166859567.90123457}]}");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = null;
            if (request.url().encodedPath().indexOf("currentStats") != -1) {
                body = ResponseBody.create(MEDIA_JSON, responseStats.toString());
            } else {
                body = ResponseBody.create(MEDIA_JSON, responseWorkers.toString());
            }
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        AccountExecutor accountExecutor = new EthermineAccountExecutor(httpClient);
        Account account = accountExecutor.getETHAccount(WALLET_ADDRESS);
        assertEquals(WALLET_ADDRESS, account.getWalletAddress());
        assertEquals(BigDecimal.valueOf(0.04401808541303854), account.getWalletBalance());
        assertEquals(543.427584, account.getTotalHashrate(), 0);
        assertEquals(3, account.getWorkerList().size());
        assertEquals("sudarman1", account.getWorkerList().get(0).getName());
        assertEquals(175.752283, account.getWorkerList().get(0).getHashrate(), 0);
        assertEquals("sudarman2", account.getWorkerList().get(1).getName());
        assertEquals(189.536898, account.getWorkerList().get(1).getHashrate(), 0);
        assertEquals("sudarman3", account.getWorkerList().get(2).getName());
        assertEquals(178.138403, account.getWorkerList().get(2).getHashrate(), 0);
    }

    @Test(expected = AccountExecutorException.class)
    public void testCorrectJsonResponseWithApiError() throws AccountExecutorException {
        JSONObject response = new JSONObject("{\"status\":\"ERROR\",\"error\":\"Invalid address\"}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        AccountExecutor accountExecutor = new EthermineAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount(WALLET_ADDRESS);
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals("Invalid address", e.getMessage());
            throw e;
        }
    }

    @Test(expected = AccountExecutorException.class)
    public void test500HttpError() throws AccountExecutorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountExecutor accountExecutor = new EthermineAccountExecutor(httpClient);
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
        AccountExecutor accountExecutor = new EthermineAccountExecutor(httpClient);
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
        AccountExecutor accountExecutor = new EthermineAccountExecutor(httpClient);
        try {
            accountExecutor.getETHAccount("");
        } catch (AccountExecutorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(errorCode, e.getMessage());
            throw e;
        }
    }

}
