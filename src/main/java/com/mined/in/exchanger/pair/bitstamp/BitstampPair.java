package com.mined.in.exchanger.pair.bitstamp;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairName;

/**
 * Class for representing Bitstamp pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BitstampPair extends Pair {

    /**
     * Creates the Bitstamp pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private BitstampPair(PairName pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Bitstamp pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Bitstamp pair instance
     */
    public static BitstampPair create(PairName pair, JSONObject jsonPair) {
        BigDecimal buyPrice = BigDecimal.valueOf(jsonPair.getDouble("bid"));
        BigDecimal sellPrice = BigDecimal.valueOf(jsonPair.getDouble("ask"));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new BitstampPair(pair, buyPrice, sellPrice);
    }

}
