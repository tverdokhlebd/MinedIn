package com.mining.profit.exchanger.currencypair.exmo;

import java.io.IOException;

import org.json.JSONObject;

import com.mining.profit.exchanger.currencypair.CurrencyPair;
import com.mining.profit.exchanger.currencypair.CurrencyPairException;
import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.exchanger.currencypair.CurrencyPairExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of Exmo executor (https://exmo.com/).
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
    public CurrencyPair getETHUSDPair() throws CurrencyPairExecutorException, CurrencyPairException {
        Request request = new Request.Builder().url(API_URL).build();
        ExmoCurrencyPair currencyPair = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CurrencyPairExecutorException(response.code());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            currencyPair = ExmoCurrencyPair.create("ETH_USD", jsonResponse);
        } catch (IOException e) {
            throw new CurrencyPairExecutorException(e);
        }
        return currencyPair;
    }

}
