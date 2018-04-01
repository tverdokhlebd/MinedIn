package com.mined.in.market.coinmarketcap;

import static com.mined.in.coin.CoinType.ETH;
import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;

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

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Endpoints update. */
    private final int endpointsUpdate;
    /** CoinMarketCap API url. */
    private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/ethereum";
    /** Next update of coin market. */
    private static Date NEXT_UPDATE;
    /** Cached coin market. */
    private static CoinMarket COIN_MARKET;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    public ETHCoinMarketRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    /**
     * Requests ETH coin market.
     *
     * @return ETH coin market
     * @throws MarketExecutorException if there is any error in request executing
     */
    public CoinMarket request() throws MarketExecutorException {
        Date currentDate = new Date();
        if (NEXT_UPDATE == null || currentDate.after(NEXT_UPDATE)) {
            Request request = new Request.Builder().url(API_URL).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new MarketExecutorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONArray(body.string()).getJSONObject(0);
                    setNextUpdate(jsonResponse);
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
     * @param jsonResponse coin market in JSON format
     */
    private void setNextUpdate(JSONObject jsonResponse) {
        Long lastUpdated = jsonResponse.getLong("last_updated");
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(lastUpdated * 1000);
        now.add(Calendar.MINUTE, endpointsUpdate);
        NEXT_UPDATE = now.getTime();
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
