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
import com.mined.in.reward.RewardExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class for retrieving ETH reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHRewardRequestor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Endpoints update. */
    private final int endpointsUpdate;
    /** WhatToMine API url. */
    private static final String API_URL = "https://whattomine.com/coins/151.json";
    /** Next update of reward. */
    private static Date NEXT_UPDATED;
    /** Cached reward. */
    private static Reward REWARD;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    public ETHRewardRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    /**
     * Requests ETH reward.
     *
     * @param hashrate reported total hashrate
     * @return ETH reward
     * @throws RewardExecutorException if there is any error in request executing
     */
    public Reward request(BigDecimal hashrate) throws RewardExecutorException {
        Date currentDate = new Date();
        if (NEXT_UPDATED == null || currentDate.after(NEXT_UPDATED)) {
            Request request = new Request.Builder().url(API_URL).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RewardExecutorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONObject(body.string());
                    setNextUpdated(jsonResponse);
                    REWARD = RewardUtil.createEstimatedReward(ETH, hashrate, jsonResponse);
                }
            } catch (JSONException e) {
                throw new RewardExecutorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new RewardExecutorException(HTTP_ERROR, e);
            }
        }
        return REWARD;
    }

    /**
     * Sets next update.
     *
     * @param jsonResponse reward in JSON format
     */
    private void setNextUpdated(JSONObject jsonResponse) {
        Long lastUpdated = jsonResponse.getLong("timestamp");
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(lastUpdated * 1000);
        now.add(Calendar.MINUTE, endpointsUpdate);
        NEXT_UPDATED = now.getTime();
    }

}
