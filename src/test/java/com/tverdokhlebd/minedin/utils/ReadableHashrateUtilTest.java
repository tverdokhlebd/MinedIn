package com.tverdokhlebd.minedin.utils;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.tverdokhlebd.minedin.utils.ReadableHashrateUtil;

/**
 * Tests of readable hashrate util.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class ReadableHashrateUtilTest {

    @Test
    public void testConvertingToKH() {
        BigDecimal hashrateWith1Digits = BigDecimal.valueOf(9);
        assertEquals("0.00 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith1Digits));
        BigDecimal hashrateWith2Digits = BigDecimal.valueOf(93);
        assertEquals("0.09 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith2Digits));
        BigDecimal hashrateWith3Digits = BigDecimal.valueOf(936);
        assertEquals("0.93 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith3Digits));
        BigDecimal hashrateWith4Digits = BigDecimal.valueOf(9367);
        assertEquals("9.36 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith4Digits));
        BigDecimal hashrateWith5Digits = BigDecimal.valueOf(93670);
        assertEquals("93.67 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith5Digits));
        BigDecimal hashrateWith6Digits = BigDecimal.valueOf(936703);
        assertEquals("936.70 kH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith6Digits));
    }

    @Test
    public void testConvertingToMH() {
        BigDecimal hashrateWith7Digits = BigDecimal.valueOf(9367032);
        assertEquals("9.36 MH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith7Digits));
        BigDecimal hashrateWith8Digits = BigDecimal.valueOf(93670321);
        assertEquals("93.67 MH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith8Digits));
        BigDecimal hashrateWith9Digits = BigDecimal.valueOf(936703211);
        assertEquals("936.70 MH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith9Digits));
    }

    @Test
    public void testConvertingToGH() {
        BigDecimal hashrateWith10Digits = BigDecimal.valueOf(9367032491L);
        assertEquals("9.36 GH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith10Digits));
        BigDecimal hashrateWith11Digits = BigDecimal.valueOf(93670324911L);
        assertEquals("93.67 GH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith11Digits));
        BigDecimal hashrateWith12Digits = BigDecimal.valueOf(936703249111L);
        assertEquals("936.70 GH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith12Digits));
    }

    @Test
    public void testConvertingToTH() {
        BigDecimal hashrateWith13Digits = BigDecimal.valueOf(9367032491111L);
        assertEquals("9.36 TH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith13Digits));
        BigDecimal hashrateWith14Digits = BigDecimal.valueOf(93670324911111L);
        assertEquals("93.67 TH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith14Digits));
        BigDecimal hashrateWith15Digits = BigDecimal.valueOf(936703249111111L);
        assertEquals("936.70 TH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith15Digits));
    }

    @Test
    public void testConvertingToPH() {
        BigDecimal hashrateWith16Digits = BigDecimal.valueOf(9367032491111111L);
        assertEquals("9.36 PH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith16Digits));
        BigDecimal hashrateWith17Digits = BigDecimal.valueOf(93670324911111111L);
        assertEquals("93.67 PH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith17Digits));
        BigDecimal hashrateWith18Digits = BigDecimal.valueOf(936703249111111111L);
        assertEquals("936.70 PH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith18Digits));
    }

    @Test
    public void testConvertingToEH() {
        BigDecimal hashrateWith19Digits = BigDecimal.valueOf(9.367032491111111111e+18);
        assertEquals("9.36 EH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith19Digits));
        BigDecimal hashrateWith20Digits = BigDecimal.valueOf(9.3670324911111111111e+19);
        assertEquals("93.67 EH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith20Digits));
        BigDecimal hashrateWith21Digits = BigDecimal.valueOf(9.36703249111111111111e+20);
        assertEquals("936.70 EH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith21Digits));
        BigDecimal hashrateWith22Digits = BigDecimal.valueOf(9.367032491111111111111e+21);
        assertEquals("9367.03 EH/s", ReadableHashrateUtil.convertToReadableHashPower(hashrateWith22Digits));
    }

}
