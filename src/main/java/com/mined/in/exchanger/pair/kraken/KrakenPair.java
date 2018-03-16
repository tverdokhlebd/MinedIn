package com.mined.in.exchanger.pair.kraken;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairName;

/**
 * Class for representing Kraken pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class KrakenPair extends Pair {

    /**
     * Creates the Kraken pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private KrakenPair(PairName pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Kraken pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Kraken pair instance
     */
    public static KrakenPair create(PairName pair, JSONObject jsonPair) {
        JSONObject ethUsd = jsonPair.getJSONObject("result").getJSONObject("XETHZUSD");
        BigDecimal buyPrice = BigDecimal.valueOf(ethUsd.getJSONArray("b").getDouble(0));
        BigDecimal sellPrice = BigDecimal.valueOf(ethUsd.getJSONArray("a").getDouble(0));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new KrakenPair(pair, buyPrice, sellPrice);
    }

}
