package com.mined.in.pool.ethermine;

import static com.mined.in.http.ErrorCode.API_ERROR;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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
 * Ethermine account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthermineAccountRequestor implements AccountRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Use accounts caching or not. */
    private final boolean useAccountCaching;
    /** API statistic url. */
    private static final String API_STATS_URL = "https://api.ethermine.org/miner/:miner/currentStats";
    /** Ether value. */
    private static final BigDecimal ETHER = BigDecimal.valueOf(1_000_000_000_000_000_000L);
    /** Cached accounts. */
    private static final Map<String, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Two minutes for repeated task. */
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    /** Repeated task for removing cached accounts. */
    static {
        TimerTask repeatedTask = new TimerTask() {

            @Override
            public void run() {
                Date currentDate = new Date();
                ACCOUNT_MAP.entrySet().removeIf(t -> currentDate.after(t.getValue().getValue()));
            }
        };
        Timer timer = new Timer(EthermineAccountRequestor.class.getName(), true);
        timer.scheduleAtFixedRate(repeatedTask, TWO_MINUTES, TWO_MINUTES);
    }

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public EthermineAccountRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = true;
    }

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     */
    public EthermineAccountRequestor(OkHttpClient httpClient, boolean useAccountCaching) {
        super();
        this.httpClient = httpClient;
        this.useAccountCaching = useAccountCaching;
    }

    @Override
    public Account requestEthereumAccount(String walletAddress) throws AccountRequestorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountRequestorException(API_ERROR, "BAD_WALLET");
        }
        if (useAccountCaching) {
            SimpleEntry<Account, Date> entry = ACCOUNT_MAP.get(walletAddress);
            if (entry == null) {
                Account account = requestAccount(walletAddress);
                entry = new SimpleEntry<Account, Date>(account, setNextRemoveDate());
                ACCOUNT_MAP.put(walletAddress, entry);
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
        walletBalance = walletBalance.divide(ETHER);
        BigDecimal reportedHashrate = BigDecimal.valueOf(data.getDouble("reportedHashrate"));
        return new Account(walletAddress, walletBalance, reportedHashrate);
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

    /**
     * Sets date of next remove.
     */
    private Date setNextRemoveDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, TWO_MINUTES);
        return now.getTime();
    }

}
