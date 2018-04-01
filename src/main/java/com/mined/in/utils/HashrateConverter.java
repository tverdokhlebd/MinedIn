package com.mined.in.utils;

import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * Utils for converting hashrate to readable hash power.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class HashrateConverter {

    /** 1 kH/s is 1,000 hashes per second. */
    private static final BigDecimal KH_S = BigDecimal.valueOf(1_000);
    /** 1 MH/s is 1,000,000 hashes per second. */
    private static final BigDecimal MH_S = BigDecimal.valueOf(1_000_000);
    /** 1 GH/s is 1,000,000,000 hashes per second. */
    private static final BigDecimal GH_S = BigDecimal.valueOf(1_000_000_000);
    /** 1 TH/s is 1,000,000,000,000 hashes per second. */
    private static final BigDecimal TH_S = BigDecimal.valueOf(1_000_000_000_000L);
    /** 1 PH/s is 1,000,000,000,000,000 hashes per second. */
    private static final BigDecimal PH_S = BigDecimal.valueOf(1_000_000_000_000_000L);
    /** 1 EH/s is 1,000,000,000,000,000,000 hashes per second. */
    private static final BigDecimal EH_S = BigDecimal.valueOf(1_000_000_000_000_000_000L);
    /** Digits number of 1 kH/s. */
    private static final int KH_S_DIGITS_NUMBER = KH_S.toPlainString().length();
    /** Digits number of 1 MH/s. */
    private static final int MH_S_DIGITS_NUMBER = MH_S.toPlainString().length();
    /** Digits number of 1 GH/s. */
    private static final int GH_S_DIGITS_NUMBER = GH_S.toPlainString().length();
    /** Digits number of 1 TH/s. */
    private static final int TH_S_DIGITS_NUMBER = TH_S.toPlainString().length();
    /** Digits number of 1 PH/s. */
    private static final int PH_S_DIGITS_NUMBER = PH_S.toPlainString().length();
    /** Text resources. */
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("text");

    /**
     * Converts hashrate to readable hash power.
     *
     * @param hashrate hashrate in H/s
     * @return readable hash power
     */
    public static String convertToReadableHashPower(BigDecimal hashrate) {
        int digitsNumber = hashrate.setScale(0, DOWN).toPlainString().length();
        if (digitsNumber <= KH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(KH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("kh_s");
        }
        if (digitsNumber >= MH_S_DIGITS_NUMBER && digitsNumber <= MH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(MH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("mh_s");
        }
        if (digitsNumber >= GH_S_DIGITS_NUMBER && digitsNumber <= GH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(GH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("gh_s");
        }
        if (digitsNumber >= TH_S_DIGITS_NUMBER && digitsNumber <= TH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(TH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("th_s");
        }
        if (digitsNumber >= PH_S_DIGITS_NUMBER && digitsNumber <= PH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(PH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("ph_s");
        }
        return hashrate.divide(EH_S, 2, DOWN).toPlainString() + " " + RESOURCE.getString("eh_s");
    }

}
