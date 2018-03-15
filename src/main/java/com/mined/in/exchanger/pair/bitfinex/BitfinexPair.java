package com.mined.in.exchanger.pair.bitfinex;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;

/**
 * Class for representing Bitfinex pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BitfinexPair extends Pair {

    /**
     * Creates the Bitfinex pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private BitfinexPair(String pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Bitfinex pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Bitfinex pair instance
     */
    public static BitfinexPair create(String pair, JSONObject jsonPair) {
        BigDecimal buyPrice = BigDecimal.valueOf(jsonPair.getDouble("bid"));
        BigDecimal sellPrice = BigDecimal.valueOf(jsonPair.getDouble("ask"));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new BitfinexPair(pair, buyPrice, sellPrice);
    }

}
