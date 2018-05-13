package com.mined.in.utils;

import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import com.tverdokhlebd.mining.commons.utils.HashrateUtils;

/**
 * Converting hashrate to readable hash power format.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ReadableHashrateUtil {

    /** Digits number of 1 kH/s. */
    private static final int KH_S_DIGITS_NUMBER = HashrateUtils.KH_S.toPlainString().length();
    /** Digits number of 1 MH/s. */
    private static final int MH_S_DIGITS_NUMBER = HashrateUtils.MH_S.toPlainString().length();
    /** Digits number of 1 GH/s. */
    private static final int GH_S_DIGITS_NUMBER = HashrateUtils.GH_S.toPlainString().length();
    /** Digits number of 1 TH/s. */
    private static final int TH_S_DIGITS_NUMBER = HashrateUtils.TH_S.toPlainString().length();
    /** Digits number of 1 PH/s. */
    private static final int PH_S_DIGITS_NUMBER = HashrateUtils.PH_S.toPlainString().length();
    /** Text resources. */
    private final static ResourceBundle RESOURCES = ResourceBundle.getBundle(ReadableHashrateUtil.class.getName());

    /**
     * Converts hashrate to readable hash power format.
     *
     * @param hashrate hashrate in H/s
     * @return readable hash power format
     */
    public static String convertToReadableHashPower(BigDecimal hashrate) {
        int digitsNumber = hashrate.setScale(0, DOWN).toPlainString().length();
        if (digitsNumber <= KH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(HashrateUtils.KH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("kh_s");
        }
        if (digitsNumber >= MH_S_DIGITS_NUMBER && digitsNumber <= MH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(HashrateUtils.MH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("mh_s");
        }
        if (digitsNumber >= GH_S_DIGITS_NUMBER && digitsNumber <= GH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(HashrateUtils.GH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("gh_s");
        }
        if (digitsNumber >= TH_S_DIGITS_NUMBER && digitsNumber <= TH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(HashrateUtils.TH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("th_s");
        }
        if (digitsNumber >= PH_S_DIGITS_NUMBER && digitsNumber <= PH_S_DIGITS_NUMBER + 2) {
            return hashrate.divide(HashrateUtils.PH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("ph_s");
        }
        return hashrate.divide(HashrateUtils.EH_S, 2, DOWN).toPlainString() + " " + RESOURCES.getString("eh_s");
    }

}
