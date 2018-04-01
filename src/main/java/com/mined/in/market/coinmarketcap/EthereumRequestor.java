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
import com.mined.in.market.MarketRequestorException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Requestor of ethereum coin market.
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
    public EthereumRequestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    /**
     * Requests ethereum coin market.
     *
     * @return ethereum coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    public CoinMarket request() throws MarketRequestorException {
        Date currentDate = new Date();
        if (NEXT_UPDATE == null || currentDate.after(NEXT_UPDATE)) {
            Request request = new Request.Builder().url(API_URL).build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new MarketRequestorException(HTTP_ERROR, response.message());
                }
                try (ResponseBody body = response.body()) {
                    JSONObject jsonResponse = new JSONArray(body.string()).getJSONObject(0);
                    setNextUpdate(jsonResponse);
                    createCoinMarket(jsonResponse);
                }
            } catch (JSONException e) {
                throw new MarketRequestorException(JSON_ERROR, e);
            } catch (IOException e) {
                throw new MarketRequestorException(HTTP_ERROR, e);
            }
        }
        return COIN_MARKET;
    }

    /**
     * Sets next update.
     *
     * @param jsonResponse JSON response
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
     * @param jsonResponse JSON response
     */
    private void createCoinMarket(JSONObject jsonResponse) {
        BigDecimal price = BigDecimal.valueOf(jsonResponse.getDouble("price_usd"));
        COIN_MARKET = new CoinMarket(ETH, price);
    }

}
