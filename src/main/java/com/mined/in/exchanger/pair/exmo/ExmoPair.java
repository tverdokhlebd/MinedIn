package com.mined.in.exchanger.pair.exmo;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;

/**
 * Class for representing Exmo pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ExmoPair extends Pair {

    /**
     * Creates the Exmo pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private ExmoPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Exmo pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPairs pair in JSON format
     * @return Exmo pair instance
     */
    public static ExmoPair create(String pair, JSONObject jsonPairs) {
        JSONObject jsonPair = jsonPairs.getJSONObject("ETH_USD");
        BigDecimal buyPrice = new BigDecimal(jsonPair.getString("buy_price"));
        BigDecimal sellPrice = new BigDecimal(jsonPair.getString("sell_price"));
        return new ExmoPair(pair, buyPrice, sellPrice);
    }

}
