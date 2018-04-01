package com.mined.in.reward.whattomine;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Requestor of ethereum estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthereumRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Endpoints update. */
    private final int endpointsUpdate;
    /** API url. */
    private static final String API_URL = "https://whattomine.com/coins/151.json";
    /** Next update of estimated reward. */
    private static Date NEXT_UPDATE;
    /** Cached estimated reward. */
    private static Reward REWARD;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    public EthereumRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    /**
     * Requests estimated reward for ethereum.
     *
     * @param hashrate reported total hashrate
     * @return estimated reward for ethereum
     * @throws RewardRequestorException if there is any error in request executing
     */
    public Reward request(BigDecimal hashrate) throws RewardRequestorException {
        Date currentDate = new Date();
        if (NEXT_UPDATE == null || currentDate.after(NEXT_UPDATE)) {
            Request request = new Request.Builder().url(API_URL).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RewardRequestorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONObject(body.string());
                    setNextUpdate(jsonResponse);
                    REWARD = RewardUtil.createEstimatedReward(ETH, hashrate, jsonResponse);
                }
            } catch (JSONException e) {
                throw new RewardRequestorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new RewardRequestorException(HTTP_ERROR, e);
            }
        }
        return REWARD;
    }

    /**
     * Sets next update.
     *
     * @param jsonResponse JSON response
     */
    private void setNextUpdate(JSONObject jsonResponse) {
        Long lastUpdated = jsonResponse.getLong("timestamp");
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(lastUpdated * 1000);
        now.add(Calendar.MINUTE, endpointsUpdate);
        NEXT_UPDATE = now.getTime();
    }

}
