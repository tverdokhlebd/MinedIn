package com.mined.in.exchanger.pair.yobit;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;

/**
 * Class for representing Yobit pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class YobitPair extends Pair {

    /**
     * Creates the Yobit pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private YobitPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Yobit pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Yobit pair instance
     */
    public static YobitPair create(String pair, JSONObject jsonPair) {
        JSONObject ethUsd = jsonPair.getJSONObject("eth_usd");
        BigDecimal buyPrice = BigDecimal.valueOf(ethUsd.getDouble("buy"));
        BigDecimal sellPrice = BigDecimal.valueOf(ethUsd.getDouble("sell"));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new YobitPair(pair, buyPrice, sellPrice);
    }

}
