package com.mined.in.calculator.whattomine;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static java.math.RoundingMode.DOWN;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.calculator.Calculation;
import com.mined.in.calculator.Calculation.CalculationBuilder;
import com.mined.in.calculator.CalculationExecutor;
import com.mined.in.calculator.CalculationExecutorException;
import com.mined.in.coin.CoinType;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of WhatToMine executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class WhatToMineCalculationExecutor implements CalculationExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** WhatToMine API ETH url. */
    private static final String API_ETH_URL = "https://whattomine.com/coins/151.json";
    /** Hours in day. */
    private static final BigDecimal HOURS_IN_DAY = BigDecimal.valueOf(24);
    /** Days in week. */
    private static final BigDecimal DAYS_IN_WEEK = BigDecimal.valueOf(7);
    /** Days in month. */
    private static final BigDecimal DAYS_IN_MONTH = BigDecimal.valueOf(30);
    /** Days in year. */
    private static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);

    /**
     * Creates the WhatToMine executor instance.
     *
     * @param httpClient HTTP client
     */
    public WhatToMineCalculationExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Calculation getETHCalculation(BigDecimal hashrate) throws CalculationExecutorException {
        Request request = new Request.Builder().url(API_ETH_URL).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CalculationExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONObject jsonResponse = new JSONObject(body.string());
                return createCalculation(ETH, hashrate, jsonResponse);
            }
        } catch (JSONException e) {
            throw new CalculationExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new CalculationExecutorException(HTTP_ERROR, e);
        }
    }

    /**
     * Creates mining calculation from JSON response.
     *
     * @param coinType type of coin
     * @param hashrate reported total hashrate
     * @param jsonResponse JSON response
     * @return mining calculation from JSON response
     */
    private Calculation createCalculation(CoinType coinType, BigDecimal hashrate, JSONObject jsonResponse) {
        Calculation.CalculationBuilder builder = new CalculationBuilder();
        builder.coinType(coinType)
               .totalHashrate(hashrate)
               .blockTime(BigDecimal.valueOf(jsonResponse.getDouble("block_time")))
               .blockReward(BigDecimal.valueOf(jsonResponse.getDouble("block_reward")))
               .blockCount(BigDecimal.valueOf(jsonResponse.getDouble("last_block")))
               .difficulty(BigDecimal.valueOf(jsonResponse.getDouble("difficulty")))
               .networkHashrate(BigDecimal.valueOf(jsonResponse.getDouble("nethash")));
        // Base rewards based on 84.0 MH/s
        BigDecimal estimatedRewardPerDay = BigDecimal.valueOf(jsonResponse.getDouble("estimated_rewards"));
        BigDecimal calculatedRewardPerDay =
                hashrate.multiply(estimatedRewardPerDay).divide(BigDecimal.valueOf(84), DOWN);
        builder.rewardPerHour(calculatedRewardPerDay.divide(HOURS_IN_DAY, DOWN))
               .rewardPerDay(calculatedRewardPerDay)
               .rewardPerWeek(calculatedRewardPerDay.multiply(DAYS_IN_WEEK))
               .rewardPerMonth(calculatedRewardPerDay.multiply(DAYS_IN_MONTH))
               .rewardPerYear(calculatedRewardPerDay.multiply(DAYS_IN_YEAR));
        return builder.createCalculation();
    }

}
