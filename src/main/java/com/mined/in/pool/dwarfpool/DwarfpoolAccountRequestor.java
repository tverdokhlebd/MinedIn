package com.mined.in.pool.dwarfpool;

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
 * Dwarfpool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class DwarfpoolAccountRequestor implements AccountRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Dwarfpool API url. */
    private static final String API_URL = "http://dwarfpool.com/eth/api?wallet=";
    /** Cached dwarfpool accounts. */
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
        Timer timer = new Timer(DwarfpoolAccountRequestor.class.getName(), true);
        timer.scheduleAtFixedRate(repeatedTask, TWO_MINUTES, TWO_MINUTES);
    }

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     */
    public DwarfpoolAccountRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Account requestEthereumAccount(String walletAddress) throws AccountRequestorException {
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new AccountRequestorException(API_ERROR, "BAD_WALLET");
        }
        SimpleEntry<Account, Date> entry = ACCOUNT_MAP.get(walletAddress);
        if (entry == null) {
            Request request = new Request.Builder().url(API_URL + walletAddress).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new AccountRequestorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONObject(body.string());
                    checkError(jsonResponse);
                    Account account = createAccount(walletAddress, jsonResponse);
                    entry = new SimpleEntry<Account, Date>(account, setNextRemoveDate());
                    ACCOUNT_MAP.put(walletAddress, entry);
                }
            } catch (JSONException e) {
                throw new AccountRequestorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new AccountRequestorException(HTTP_ERROR, e);
            }
        }
        return entry.getKey();
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
        BigDecimal totalHashrate = BigDecimal.valueOf(jsonAccount.getDouble("total_hashrate"));
        return new Account(walletAddress, walletBalance, totalHashrate);
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

    /**
     * Sets date of next remove.
     */
    private Date setNextRemoveDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, TWO_MINUTES);
        return now.getTime();
    }

}
