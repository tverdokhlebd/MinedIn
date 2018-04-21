package com.mined.in.market.coinmarketcap;

import static com.mined.in.http.ErrorCode.HTTP_ERROR;
import static com.mined.in.http.ErrorCode.JSON_ERROR;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mined.in.coin.CoinMarket;
import com.mined.in.coin.CoinType;
import com.mined.in.http.BaseRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.utils.TimeUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Base requestor of coin market.
 *
 * @author Dmitry Tverdokhleb
 *
 */
abstract class Requestor implements BaseRequestor<Object, CoinMarket> {

    /** HTTP client. */
    private final OkHttpClient httpClient;
    /** Endpoints update. */
    private final int endpointsUpdate;
    /** Next update of coin market. */
    private static Date NEXT_UPDATE = new Date(0);
    /** Cached coin market. */
    private static CoinMarket COIN_MARKET;

    /**
     * Creates the instance.
     *
     * @param httpClient HTTP client
     * @param endpointsUpdate endpoints update
     */
    Requestor(OkHttpClient httpClient, int endpointsUpdate) {
        super();
        this.httpClient = httpClient;
        this.endpointsUpdate = endpointsUpdate;
    }

    /**
     * Requests coin market.
     *
     * @return coin market
     * @throws MarketRequestorException if there is any error in market requesting
     */
    @Override
    public CoinMarket request() throws MarketRequestorException {
        if (new Date().after(NEXT_UPDATE)) {
            Request request = new Request.Builder().url(getUrl()).build();
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

    @Override
    public CoinMarket request(Object t) throws Exception {
        return null;
    }

    /**
     * Sets next update.
     *
     * @param jsonResponse JSON response
     */
    private void setNextUpdate(JSONObject jsonResponse) {
        Date lastUpdated = new Date(jsonResponse.getLong("last_updated") * 1000);
        NEXT_UPDATE = TimeUtils.addMinutes(lastUpdated, endpointsUpdate);
    }

    /**
     * Creates coin market.
     *
     * @param jsonResponse JSON response
     */
    private void createCoinMarket(JSONObject jsonResponse) {
        BigDecimal price = BigDecimal.valueOf(jsonResponse.getDouble("price_usd"));
        CoinType coinType = CoinType.valueOf(jsonResponse.getString("symbol"));
        COIN_MARKET = new CoinMarket(coinType, price);
    }

}
