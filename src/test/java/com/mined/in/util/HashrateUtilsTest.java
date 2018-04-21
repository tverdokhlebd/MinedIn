package com.mined.in.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.utils.HashrateUtils;

/**
 * Tests of hashrate utils.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class HashrateUtilsTest {

    @Test
    public void testConvertingToKH() {
        BigDecimal hashrateWith1Digits = BigDecimal.valueOf(9);
        assertEquals("0.00 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith1Digits));
        BigDecimal hashrateWith2Digits = BigDecimal.valueOf(93);
        assertEquals("0.09 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith2Digits));
        BigDecimal hashrateWith3Digits = BigDecimal.valueOf(936);
        assertEquals("0.93 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith3Digits));
        BigDecimal hashrateWith4Digits = BigDecimal.valueOf(9367);
        assertEquals("9.36 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith4Digits));
        BigDecimal hashrateWith5Digits = BigDecimal.valueOf(93670);
        assertEquals("93.67 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith5Digits));
        BigDecimal hashrateWith6Digits = BigDecimal.valueOf(936703);
        assertEquals("936.70 kH/s", HashrateUtils.convertToReadableHashPower(hashrateWith6Digits));
    }

    @Test
    public void testConvertingToMH() {
        BigDecimal hashrateWith7Digits = BigDecimal.valueOf(9367032);
        assertEquals("9.36 MH/s", HashrateUtils.convertToReadableHashPower(hashrateWith7Digits));
        BigDecimal hashrateWith8Digits = BigDecimal.valueOf(93670321);
        assertEquals("93.67 MH/s", HashrateUtils.convertToReadableHashPower(hashrateWith8Digits));
        BigDecimal hashrateWith9Digits = BigDecimal.valueOf(936703211);
        assertEquals("936.70 MH/s", HashrateUtils.convertToReadableHashPower(hashrateWith9Digits));
    }

    @Test
    public void testConvertingToGH() {
        BigDecimal hashrateWith10Digits = BigDecimal.valueOf(9367032491L);
        assertEquals("9.36 GH/s", HashrateUtils.convertToReadableHashPower(hashrateWith10Digits));
        BigDecimal hashrateWith11Digits = BigDecimal.valueOf(93670324911L);
        assertEquals("93.67 GH/s", HashrateUtils.convertToReadableHashPower(hashrateWith11Digits));
        BigDecimal hashrateWith12Digits = BigDecimal.valueOf(936703249111L);
        assertEquals("936.70 GH/s", HashrateUtils.convertToReadableHashPower(hashrateWith12Digits));
    }

    @Test
    public void testConvertingToTH() {
        BigDecimal hashrateWith13Digits = BigDecimal.valueOf(9367032491111L);
        assertEquals("9.36 TH/s", HashrateUtils.convertToReadableHashPower(hashrateWith13Digits));
        BigDecimal hashrateWith14Digits = BigDecimal.valueOf(93670324911111L);
        assertEquals("93.67 TH/s", HashrateUtils.convertToReadableHashPower(hashrateWith14Digits));
        BigDecimal hashrateWith15Digits = BigDecimal.valueOf(936703249111111L);
        assertEquals("936.70 TH/s", HashrateUtils.convertToReadableHashPower(hashrateWith15Digits));
    }

    @Test
    public void testConvertingToPH() {
        BigDecimal hashrateWith16Digits = BigDecimal.valueOf(9367032491111111L);
        assertEquals("9.36 PH/s", HashrateUtils.convertToReadableHashPower(hashrateWith16Digits));
        BigDecimal hashrateWith17Digits = BigDecimal.valueOf(93670324911111111L);
        assertEquals("93.67 PH/s", HashrateUtils.convertToReadableHashPower(hashrateWith17Digits));
        BigDecimal hashrateWith18Digits = BigDecimal.valueOf(936703249111111111L);
        assertEquals("936.70 PH/s", HashrateUtils.convertToReadableHashPower(hashrateWith18Digits));
    }

    @Test
    public void testConvertingToEH() {
        BigDecimal hashrateWith19Digits = BigDecimal.valueOf(9.367032491111111111e+18);
        assertEquals("9.36 EH/s", HashrateUtils.convertToReadableHashPower(hashrateWith19Digits));
        BigDecimal hashrateWith20Digits = BigDecimal.valueOf(9.3670324911111111111e+19);
        assertEquals("93.67 EH/s", HashrateUtils.convertToReadableHashPower(hashrateWith20Digits));
        BigDecimal hashrateWith21Digits = BigDecimal.valueOf(9.36703249111111111111e+20);
        assertEquals("936.70 EH/s", HashrateUtils.convertToReadableHashPower(hashrateWith21Digits));
        BigDecimal hashrateWith22Digits = BigDecimal.valueOf(9.367032491111111111111e+21);
        assertEquals("9367.03 EH/s", HashrateUtils.convertToReadableHashPower(hashrateWith22Digits));
    }

    @Test
    public void testConvertingMegaHashesToHashes() {
        BigDecimal megaHashes1 = BigDecimal.valueOf(174.05);
        assertEquals(BigDecimal.valueOf(174050000), HashrateUtils.convertMegaHashesToHashes(megaHashes1));
        BigDecimal megaHashes2 = BigDecimal.valueOf(174);
        assertEquals(BigDecimal.valueOf(174000000), HashrateUtils.convertMegaHashesToHashes(megaHashes2));
        BigDecimal megaHashes3 = BigDecimal.valueOf(1740.5);
        assertEquals(BigDecimal.valueOf(1740500000), HashrateUtils.convertMegaHashesToHashes(megaHashes3));
    }

    @Test
    public void testConvertingHashesToMegaHashes() {
        BigDecimal hashes1 = BigDecimal.valueOf(174050000);
        assertEquals(BigDecimal.valueOf(174.05), HashrateUtils.convertHashesToMegaHashes(hashes1));
        BigDecimal hashes2 = BigDecimal.valueOf(174000000);
        assertEquals(BigDecimal.valueOf(174), HashrateUtils.convertHashesToMegaHashes(hashes2));
        BigDecimal hashes3 = BigDecimal.valueOf(1740517405);
        assertEquals(BigDecimal.valueOf(1740.517405), HashrateUtils.convertHashesToMegaHashes(hashes3));
    }

}
