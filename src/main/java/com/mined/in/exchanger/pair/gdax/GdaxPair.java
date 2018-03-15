package com.mined.in.exchanger.pair.gdax;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;

/**
 * Class for representing Gdax pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class GdaxPair extends Pair {

    /**
     * Creates the Gdax pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private GdaxPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Gdax pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Gdax pair instance
     */
    public static GdaxPair create(String pair, JSONObject jsonPair) {
        BigDecimal buyPrice = BigDecimal.valueOf(jsonPair.getDouble("bid"));
        BigDecimal sellPrice = BigDecimal.valueOf(jsonPair.getDouble("ask"));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new GdaxPair(pair, buyPrice, sellPrice);
    }

}
