package com.mined.in.pool.nanopool;

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

import com.mined.in.http.BaseRequestor;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountCaching;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.utils.HashrateUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Base nanopool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
abstract class Requestor implements BaseRequestor<String, Account>, AccountCaching {

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
    public Requestor(OkHttpClient httpClient, boolean useAccountCaching) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = useAccountCaching;
    }

    @Override
    public Account request() throws Exception {
        return null;
    }

    @Override
    public Account request(String walletAddress) throws AccountRequestorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountRequestorException(API_ERROR, "BAD_WALLET");
        }
        if (useAccountCaching) {
            SimpleEntry<Account, Date> entry = getCachedAccountMap().get(walletAddress);
            if (entry == null) {
                Account account = requestAccount(walletAddress);
                entry = new SimpleEntry<Account, Date>(account, addMinutes(new Date(), 2));
                getCachedAccountMap().put(walletAddress, entry);
            }
            return entry.getKey();
        } else {
            return requestAccount(walletAddress);
        }
    }

    /**
     * Requests ethereum pool account.
     *
     * @param walletAddress the wallet address
     * @return ethereum pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    private Account requestAccount(String walletAddress) throws AccountRequestorException {
        Account account = requestAccountWithBalance(walletAddress);
        BigDecimal reportedHashrate = requestReportedHashrate(walletAddress);
        reportedHashrate = HashrateUtils.convertMegaHashesToHashes(reportedHashrate);
        account.setReportedHashrate(reportedHashrate);
        return account;
    }

    /**
     * Requests account with balance.
     *
     * @param walletAddress wallet address
     * @return account with balance
     * @throws AccountRequestorException if there is any error in account requesting
     */
    private Account requestAccountWithBalance(String walletAddress) throws AccountRequestorException {
        Request request = new Request.Builder().url(getUrl() + "/balance/" + walletAddress).build();
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
     * Requests reported hashrate.
     *
     * @param walletAddress wallet address
     * @return reported hashrate
     * @throws AccountRequestorException if there is any error in account requesting
     */
    private BigDecimal requestReportedHashrate(String walletAddress) throws AccountRequestorException {
        Request request = new Request.Builder().url(getUrl() + "/reportedhashrate/" + walletAddress).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new AccountRequestorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                checkError(jsonResponse);
                return BigDecimal.valueOf(jsonResponse.getDouble("data"));
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
        BigDecimal walletBalance = BigDecimal.valueOf(jsonAccount.getDouble("data"));
        return new Account(walletAddress, walletBalance, null);
    }

    /**
     * Check presence of error in JSON response.
     *
     * @param jsonResponse response in JSON format
     * @throws AccountRequestorException if there is any error in JSON response
     */
    private void checkError(JSONObject jsonResponse) throws AccountRequestorException {
        boolean status = jsonResponse.getBoolean("status");
        if (!status) {
            String errorMessage = jsonResponse.getString("error");
            throw new AccountRequestorException(API_ERROR, errorMessage);
        }
    }

}
