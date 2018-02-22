package com.mining.profit.exchanger.currencypair.exmo;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.mining.profit.exchanger.currencypair.CurrencyPair;
import com.mining.profit.exchanger.currencypair.CurrencyPairException;

/**
 * Class for representing Exmo currency pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ExmoCurrencyPair extends CurrencyPair {

    /**
     * Creates the Exmo currency pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private ExmoCurrencyPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Exmo currency pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPairs currency pairs in JSON format
     * @return Exmo currency pair instance
     * @throws CurrencyPairException if there is any error in currency pair creating
     */
    public static ExmoCurrencyPair create(String pair, JSONObject jsonPairs) throws CurrencyPairException {
        try {
            JSONObject jsonPair = jsonPairs.getJSONObject("ETH_USD");
            BigDecimal buyPrice = new BigDecimal(jsonPair.getString("buy_price"));
            BigDecimal sellPrice = new BigDecimal(jsonPair.getString("sell_price"));
            return new ExmoCurrencyPair(pair, buyPrice, sellPrice);
        } catch (JSONException e) {
            throw new CurrencyPairException(e);
        }
    }

}
