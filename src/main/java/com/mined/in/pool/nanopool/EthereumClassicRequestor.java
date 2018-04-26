package com.mined.in.pool.nanopool;

import static com.mined.in.utils.TaskUtils.startRepeatedTask;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.mined.in.pool.Account;
import com.mined.in.utils.HashrateUtils;

import okhttp3.OkHttpClient;

/**
 * Ethereum classic account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class EthereumClassicRequestor extends Requestor {

    /** API url. */
    private static final String API_URL = "https://api.nanopool.org/v1/etc";
    /** Cached accounts. */
    private static final Map<String, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Repeated task for removing cached accounts. */
    static {
        startRepeatedTask(EthereumClassicRequestor.class.getName(), new TimerTask() {

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
    EthereumClassicRequestor(OkHttpClient httpClient, boolean useAccountCaching) {
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

    @Override
    protected BigDecimal convertToHashes(BigDecimal reportedHashrate) {
        return HashrateUtils.convertMegaHashesToHashes(reportedHashrate);
    }

}
