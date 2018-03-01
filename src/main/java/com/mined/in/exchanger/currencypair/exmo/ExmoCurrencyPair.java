package com.mined.in.exchanger.currencypair.exmo;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.currencypair.CurrencyPair;

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
     */
    public static ExmoCurrencyPair create(String pair, JSONObject jsonPairs) {
        JSONObject jsonPair = jsonPairs.getJSONObject("ETH_USD");
        BigDecimal buyPrice = new BigDecimal(jsonPair.getString("buy_price"));
        BigDecimal sellPrice = new BigDecimal(jsonPair.getString("sell_price"));
        return new ExmoCurrencyPair(pair, buyPrice, sellPrice);
    }

}
