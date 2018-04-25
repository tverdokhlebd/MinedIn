package com.mined.in.pool.dwarfpool;

import static com.mined.in.utils.TaskUtils.startRepeatedTask;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.mined.in.pool.Account;

import okhttp3.OkHttpClient;

/**
 * Monero account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class MoneroRequestor extends Requestor {

    /** API url. */
    private static final String API_URL = "http://dwarfpool.com/xmr/api?wallet=";
    /** Cached accounts. */
    private static final Map<String, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Repeated task for removing cached accounts. */
    static {
        startRepeatedTask(MoneroRequestor.class.getName(), new TimerTask() {

            @Override
            public void run() {
                Date currentDate = new Date();
                ACCOUNT_MAP.entrySet().removeIf(t -> currentDate.after(t.getValue().getValue()));
            }
        }, MINUTES.toMillis(2));
    }

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     */
    MoneroRequestor(OkHttpClient httpClient, boolean useAccountCaching) {
        super(httpClient, useAccountCaching);
    }

    @Override
    public String getUrl() {
        return API_URL;
    }

    @Override
    public Map<String, SimpleEntry<Account, Date>> getCachedAccountMap() {
        return ACCOUNT_MAP;
    }

}
