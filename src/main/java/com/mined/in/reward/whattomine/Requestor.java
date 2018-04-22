package com.mined.in.reward.whattomine;

import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static com.mined.in.utils.TimeUtils.DAYS_IN_MONTH;
import static com.mined.in.utils.TimeUtils.DAYS_IN_WEEK;
import static com.mined.in.utils.TimeUtils.DAYS_IN_YEAR;
import static com.mined.in.utils.TimeUtils.HOURS_IN_DAY;
import static java.math.RoundingMode.DOWN;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinInfo.CoinInfoBuilder;
import com.mined.in.coin.CoinType;
import com.mined.in.reward.BaseRewardRequestor;
import com.mined.in.reward.Reward;
import com.mined.in.reward.Reward.Builder;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.utils.TimeUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Requestor of estimated reward.
 *
 * @author Dmitry Tverdokhleb
 *
 */
abstract class Requestor implements BaseRewardRequestor<BigDecimal, Reward> {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Endpoints update. */
    private final int endpointsUpdate;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    Requestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    @Override
    public Reward request() throws Exception {
        return null;
    }

    /**
     * Requests estimated reward.
     *
     * @param hashrate reported hashrate in H/s
     * @return estimated reward
     * @throws RewardRequestorException if there is any error in request executing
     */
    @Override
    public Reward request(BigDecimal hashrate) throws RewardRequestorException {
        if (new Date().after(getCachedNextUpdate())) {
            Request request = new Request.Builder().url(getUrl()).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RewardRequestorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONObject(body.string());
                    setNextUpdate(jsonResponse);
                    createCoinInfo(jsonResponse);
                    setCachedEstimatedRewardPerDay(BigDecimal.valueOf(jsonResponse.getDouble("estimated_rewards")));
                }
            } catch (JSONException e) {
                throw new RewardRequestorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new RewardRequestorException(HTTP_ERROR, e);
            }
        }
        return calculateEstimatedReward(hashrate);
    }

    /**
     * Creates coin information.
     *
     * @param jsonResponse jsonResponse JSON response
     */
    private void createCoinInfo(JSONObject jsonResponse) {
        CoinInfo.CoinInfoBuilder coinBuilder = new CoinInfoBuilder();
        coinBuilder.coinType(CoinType.valueOf(jsonResponse.getString("tag")))
                   .blockTime(BigDecimal.valueOf(jsonResponse.getDouble("block_time")))
                   .blockReward(BigDecimal.valueOf(jsonResponse.getDouble("block_reward")))
                   .blockCount(BigDecimal.valueOf(jsonResponse.getDouble("last_block")))
                   .difficulty(BigDecimal.valueOf(jsonResponse.getDouble("difficulty")))
                   .networkHashrate(BigDecimal.valueOf(jsonResponse.getDouble("nethash")));
        setCachedCoinInfo(coinBuilder.build());
    }

    /**
     * Calculates estimated reward.
     *
     * @param hashrate reported hashrate in H/s
     * @return estimated reward
     */
    private Reward calculateEstimatedReward(BigDecimal hashrate) {
        Reward.Builder rewardBuilder = new Builder();
        rewardBuilder.coinInfo(getCachedCoinInfo());
        if (hashrate != null) {
            BigDecimal calculatedRewardPerDay =
                    hashrate.multiply(getCachedEstimatedRewardPerDay()).divide(getBaseReward(), 6, DOWN);
            rewardBuilder.setReportedHashrate(hashrate)
                         .rewardPerHour(calculatedRewardPerDay.divide(HOURS_IN_DAY, DOWN))
                         .rewardPerDay(calculatedRewardPerDay)
                         .rewardPerWeek(calculatedRewardPerDay.multiply(DAYS_IN_WEEK))
                         .rewardPerMonth(calculatedRewardPerDay.multiply(DAYS_IN_MONTH))
                         .rewardPerYear(calculatedRewardPerDay.multiply(DAYS_IN_YEAR));
        }
        return rewardBuilder.build();
    }

    /**
     * Sets next update.
     *
     * @param jsonResponse JSON response
     */
    private void setNextUpdate(JSONObject jsonResponse) {
        Date lastUpdated = new Date(jsonResponse.getLong("timestamp") * 1000);
        setCachedNextUpdate(TimeUtils.addMinutes(lastUpdated, endpointsUpdate));
    }

}
