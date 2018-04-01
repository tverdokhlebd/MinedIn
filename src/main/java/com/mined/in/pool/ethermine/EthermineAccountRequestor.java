package com.mined.in.pool.ethermine;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of Ethermine requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthermineAccountRequestor implements AccountRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Ethermine API statistic url. */
    private static final String API_STATS_URL = "https://api.ethermine.org/miner/:miner/currentStats";
    /** Wei. */
    private final static BigDecimal WEI = BigDecimal.valueOf(1_000_000_000_000_000_000L);
    /** Megahash. */
    private final static int MEGAHASH = 1_000_000;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public EthermineAccountRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Account getETHAccount(String walletAddress) throws AccountRequestorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountRequestorException(API_ERROR, "BAD_WALLET");
        }
        Request request = new Request.Builder().url(API_STATS_URL.replace(":miner", walletAddress)).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountRequestorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                return createAccount(walletAddress, jsonResponse);
            }
        } catch (JSONException e) {
            throw new AccountRequestorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new AccountRequestorException(HTTP_ERROR, e);
        }
    }

    /**
     * Creates account from JSON response.
     *
     * @param walletAddress wallet address
     * @param jsonAccount account in JSON format
     * @return account
     */
    private Account createAccount(String walletAddress, JSONObject jsonAccount) {
        JSONObject data = jsonAccount.getJSONObject("data");
        BigDecimal walletBalance = BigDecimal.valueOf(data.getLong("unpaid"));
        walletBalance = walletBalance.divide(WEI);
        BigDecimal totalHashrate = BigDecimal.valueOf(data.getDouble("reportedHashrate") / MEGAHASH);
        return new Account(walletAddress, walletBalance, totalHashrate);
    }

    /**
     * Check presence of error in JSON response.
     *
     * @param jsonResponse response in JSON format
     * @throws AccountRequestorException if there is any error in JSON response
     */
    private void checkError(JSONObject jsonResponse) throws AccountRequestorException {
        String status = jsonResponse.getString("status");
        if (status.equalsIgnoreCase("error")) {
            String errorMessage = jsonResponse.getString("error");
            throw new AccountRequestorException(API_ERROR, errorMessage);
        }
    }

}