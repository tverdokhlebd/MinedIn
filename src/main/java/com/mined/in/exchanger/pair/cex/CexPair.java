package com.mined.in.exchanger.pair.cex;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairName;

/**
 * Class for representing Cex pair.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CexPair extends Pair {

    /**
     * Creates the Cex pair instance.
     *
     * @param pair pair name
     * @param buyPrice buy price
     * @param sellPrice sell price
     */
    private CexPair(PairName pair, BigDecimal buyPrice, BigDecimal sellPrice) {
        super(pair, buyPrice, sellPrice);
    }

    /**
     * Creates the Cex pair instance from JSON format.
     *
     * @param pair pair name
     * @param jsonPair pair in JSON format
     * @return Cex pair instance
     */
    public static CexPair create(PairName pair, JSONObject jsonPair) {
        BigDecimal buyPrice = BigDecimal.valueOf(jsonPair.getDouble("bid"));
        BigDecimal sellPrice = BigDecimal.valueOf(jsonPair.getDouble("ask"));
        buyPrice = buyPrice.stripTrailingZeros();
        sellPrice = sellPrice.stripTrailingZeros();
        return new CexPair(pair, buyPrice, sellPrice);
    }

}
