package com.mined.in.reward.whattomine;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;
import static java.math.RoundingMode.DOWN;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinInfo.CoinInfoBuilder;
import com.mined.in.coin.CoinType;
import com.mined.in.reward.Reward;
import com.mined.in.reward.Reward.Builder;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.utils.HashrateConverter;

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
    /** Hours in day. */
    private static final BigDecimal HOURS_IN_DAY = BigDecimal.valueOf(24);
    /** Days in week. */
    private static final BigDecimal DAYS_IN_WEEK = BigDecimal.valueOf(7);
    /** Days in month. */
    private static final BigDecimal DAYS_IN_MONTH = BigDecimal.valueOf(30);
    /** Days in year. */
    private static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);

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
                    REWARD = createEstimatedReward(ETH, hashrate, jsonResponse);
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
     * Creates estimated reward from JSON response.
     *
     * @param coinType type of coin
     * @param hashrate reported total hashrate
     * @param jsonResponse JSON response
     * @return estimated reward
     */
    public Reward createEstimatedReward(CoinType coinType, BigDecimal hashrate, JSONObject jsonResponse) {
        CoinInfo.CoinInfoBuilder coinBuilder = new CoinInfoBuilder();
        coinBuilder.coinType(coinType)
                   .blockTime(BigDecimal.valueOf(jsonResponse.getDouble("block_time")))
                   .blockReward(BigDecimal.valueOf(jsonResponse.getDouble("block_reward")))
                   .blockCount(BigDecimal.valueOf(jsonResponse.getDouble("last_block")))
                   .difficulty(BigDecimal.valueOf(jsonResponse.getDouble("difficulty")))
                   .networkHashrate(BigDecimal.valueOf(jsonResponse.getDouble("nethash")));
        CoinInfo coinInfo = coinBuilder.build();
        BigDecimal hashrateInMegahashes = HashrateConverter.convertHashesToMegaHashes(hashrate);
        BigDecimal estimatedRewardPerDay = BigDecimal.valueOf(jsonResponse.getDouble("estimated_rewards"));
        // Base rewards based on 84.0 MH/s
        BigDecimal calculatedRewardPerDay = hashrateInMegahashes.multiply(estimatedRewardPerDay).divide(BigDecimal.valueOf(84), 6, DOWN);
        Reward.Builder rewardBuilder = new Builder();
        rewardBuilder.coinInfo(coinInfo)
                     .setTotalHashrate(hashrate)
                     .rewardPerHour(calculatedRewardPerDay.divide(HOURS_IN_DAY, DOWN))
                     .rewardPerDay(calculatedRewardPerDay)
                     .rewardPerWeek(calculatedRewardPerDay.multiply(DAYS_IN_WEEK))
                     .rewardPerMonth(calculatedRewardPerDay.multiply(DAYS_IN_MONTH))
                     .rewardPerYear(calculatedRewardPerDay.multiply(DAYS_IN_YEAR));
        return rewardBuilder.build();
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
