package com.mined.in.exchanger.pair.yobit;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;
import static com.mined.in.exchanger.pair.PairName.ETH_USD;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Yobit executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class YobitPairExecutor implements PairExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Yobit API url. */
    private static final String API_URL = "https://yobit.net/api/3/ticker/";

    /**
     * Creates the Yobit executor instance.
     *
     * @param httpClient HTTP client
     */
    public YobitPairExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Pair getETHUSDPair() throws PairExecutorException {
        Request request = new Request.Builder().url(API_URL + "eth_usd").build();
        YobitPair pair = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new PairExecutorException(HTTP_ERROR, response.message());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            pair = YobitPair.create(ETH_USD, jsonResponse);
        } catch (JSONException e) {
            throw new PairExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new PairExecutorException(HTTP_ERROR, e);
        }
        return pair;
    }

}
