package com.mined.in.exchanger.pair.cex;

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
 * Implementation of Cex executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CexPairExecutor implements PairExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Cex API url. */
    private static final String API_URL = "https://cex.io/api/ticker/";

    /**
     * Creates the Cex executor instance.
     *
     * @param httpClient HTTP client
     */
    public CexPairExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Pair getETHUSDPair() throws PairExecutorException {
        Request request = new Request.Builder().url(API_URL + "ETH/USD").build();
        CexPair pair = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new PairExecutorException(HTTP_ERROR, response.message());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            pair = CexPair.create(ETH_USD, jsonResponse);
        } catch (JSONException e) {
            throw new PairExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new PairExecutorException(HTTP_ERROR, e);
        }
        return pair;
    }

}
