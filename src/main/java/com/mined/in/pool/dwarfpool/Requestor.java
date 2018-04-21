package com.mined.in.pool.dwarfpool;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.utils.TimeUtils.addMinutes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.BaseAccountRequestor;
import com.mined.in.utils.HashrateUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Base dwarfpool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
abstract class Requestor implements BaseAccountRequestor<String, Account> {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Use accounts caching or not. */
    private final boolean useAccountCaching;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     */
    Requestor(OkHttpClient httpClient, boolean useAccountCaching) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = useAccountCaching;
    }

    /**
     * Request pool account.
     *
     * @param walletAddress the wallet address
     * @return pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    @Override
    public Account request(String walletAddress) throws AccountRequestorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountRequestorException(API_ERROR, "BAD_WALLET");
        }
        if (useAccountCaching) {
            SimpleEntry<Account, Date> entry = getCachedAccountMap().get(walletAddress);
            if (entry == null) {
                Account account = requestAccountFromPool(walletAddress);
                entry = new SimpleEntry<Account, Date>(account, addMinutes(new Date(), 2));
                getCachedAccountMap().put(walletAddress, entry);
            }
            return entry.getKey();
        } else {
            return requestAccountFromPool(walletAddress);
        }
    }

    /**
     * Requests pool account from pool service.
     *
     * @param walletAddress the wallet address
     * @return pool account from pool service
     * @throws AccountRequestorException if there is any error in account requesting
     */
    private Account requestAccountFromPool(String walletAddress) throws AccountRequestorException {
        Request request = new Request.Builder().url(getUrl() + walletAddress).build();
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
        BigDecimal walletBalance = BigDecimal.valueOf(jsonAccount.getDouble("wallet_balance"));
        BigDecimal reportedHashrate = BigDecimal.valueOf(jsonAccount.getDouble("total_hashrate"));
        reportedHashrate = HashrateUtils.convertMegaHashesToHashes(reportedHashrate);
        return new Account(walletAddress, walletBalance, reportedHashrate);
    }

    /**
     * Check presence of error in JSON response.
     *
     * @param jsonResponse response in JSON format
     * @throws AccountRequestorException if there is any error in JSON response
     */
    private void checkError(JSONObject jsonResponse) throws AccountRequestorException {
        boolean error = jsonResponse.getBoolean("error");
        if (error) {
            String errorCode = jsonResponse.getString("error_code");
            throw new AccountRequestorException(API_ERROR, errorCode);
        }
    }

}
