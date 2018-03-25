package com.mined.in.market.coinmarketcap;

import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.market.Market;
import com.mined.in.market.Market.MarketBuilder;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of CoinMarketCap executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CoinMarketCapMarketExecutor implements MarketExecutor {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Maximum number of tickers. */
    private static final int MAX_TICKER_NUMBER = 1;
    /** CoinMarketCap API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=0";

    /**
     * Creates the CoinMarketCap executor instance.
     *
     * @param httpClient HTTP client
     */
    public CoinMarketCapMarketExecutor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    @Override
    public Market getMarket() throws MarketExecutorException {
        Request request = new Request.Builder().url(API_URL).build();
        Market market = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new MarketExecutorException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                JSONArray jsonResponse = new JSONArray(body.string());
                market = createMarket(jsonResponse);
            }
        } catch (JSONException e) {
            throw new MarketExecutorException(JSON_ERROR, e);
        } catch (IOException e) {
            throw new MarketExecutorException(HTTP_ERROR, e);
        }
        return market;
    }

    /**
     * Creates market from JSON response.
     *
     * @param jsonResponse JSON response
     * @return market from JSON response
     */
    private Market createMarket(JSONArray jsonResponse) {
        Market.MarketBuilder builder = new MarketBuilder();
        int tickerNumber = 0;
        for (int i = 0; i < jsonResponse.length(); i++) {
            JSONObject jsonTicker = jsonResponse.getJSONObject(i);
            String symbol = jsonTicker.getString("symbol");
            switch (symbol) {
            case "ETH": {
                BigDecimal price = BigDecimal.valueOf(jsonTicker.getDouble("price_usd"));
                builder.ethPrice(price);
                tickerNumber++;
                break;
            }
            }
            if (tickerNumber == MAX_TICKER_NUMBER) {
                break;
            }
        }
        return builder.createMarket();
    }

}
