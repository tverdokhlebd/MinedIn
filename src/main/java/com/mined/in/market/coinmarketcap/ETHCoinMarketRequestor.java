package com.mined.in.market.coinmarketcap;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.error.ErrorCode.HTTP_ERROR;
import static com.mined.in.error.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketExecutorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class for retrieving ETH coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHCoinMarketRequestor {

    /** CoinMarketCap API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/ethereum";
    /** Next update of coin market. */
    private static Date NEXT_UPDATED;
    /** Cached coin market. */
    private static CoinMarket COIN_MARKET;

    /**
     * Requests ETH coin market.
     *
     * @param endpointsUpdate endpoints update
     * @param httpClient httpClient HTTP client
     * @return ETH coin market
     * @throws MarketExecutorException if there is any error in request executing
     */
    public CoinMarket request(int endpointsUpdate, OkHttpClient httpClient) throws MarketExecutorException {
        Date currentDate = new Date();
        if (NEXT_UPDATED == null || currentDate.after(NEXT_UPDATED)) {
            Request request = new Request.Builder().url(API_URL).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new MarketExecutorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONArray(body.string()).getJSONObject(0);
                    setNextUpdated(endpointsUpdate, jsonResponse);
                    createCoinMarket(jsonResponse);
                }
            } catch (JSONException e) {
                throw new MarketExecutorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new MarketExecutorException(HTTP_ERROR, e);
            }
        }
        return COIN_MARKET;
    }

    /**
     * Sets next update.
     *
     * @param endpointsUpdate endpoints update
     * @param jsonResponse coin market in JSON format
     */
    private void setNextUpdated(int endpointsUpdate, JSONObject jsonResponse) {
        Long lastUpdated = jsonResponse.getLong("last_updated");
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(lastUpdated * 1000);
        now.add(Calendar.MINUTE, endpointsUpdate);
        NEXT_UPDATED = now.getTime();
    }

    /**
     * Creates coin market.
     *
     * @param jsonResponse coin market in JSON format
     */
    private void createCoinMarket(JSONObject jsonResponse) {
        BigDecimal price = BigDecimal.valueOf(jsonResponse.getDouble("price_usd"));
        COIN_MARKET = new CoinMarket(ETH, price);
    }

}
