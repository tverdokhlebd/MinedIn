package com.mined.in.exchanger.currencypair.exmo;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.exchanger.currencypair.CurrencyPair;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutor;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Exmo executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ExmoCurrencyPairExecutor implements CurrencyPairExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Exmo API url. */
    private static final String API_URL = "https://api.exmo.com/v1/ticker/";

    /**
     * Creates the Exmo executor instance.
     *
     * @param httpClient HTTP client
     */
    public ExmoCurrencyPairExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public CurrencyPair getETHUSDPair() throws CurrencyPairExecutorException {
        Request request = new Request.Builder().url(API_URL).build();
        ExmoCurrencyPair currencyPair = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CurrencyPairExecutorException(HTTP_ERROR, response.message());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            currencyPair = ExmoCurrencyPair.create("ETH_USD", jsonResponse);
        } catch (JSONException e) {
            throw new CurrencyPairExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new CurrencyPairExecutorException(HTTP_ERROR, e);
        }
        return currencyPair;
    }

}
