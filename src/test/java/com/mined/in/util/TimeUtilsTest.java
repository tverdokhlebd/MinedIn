package com.mined.in.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.utils.TimeUtils;

/**
 * Tests of time utils.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class TimeUtilsTest {

    @Test
    public void testConvertingToTime() {
        BigDecimal sec30 = BigDecimal.valueOf(30.33);
        assertEquals("30s", TimeUtils.convertToReadableTime(sec30));
        BigDecimal sec60 = BigDecimal.valueOf(60.33);
        assertEquals("1m", TimeUtils.convertToReadableTime(sec60));
        BigDecimal sec75 = BigDecimal.valueOf(75);
        assertEquals("1m 15s", TimeUtils.convertToReadableTime(sec75));
        BigDecimal sec90 = BigDecimal.valueOf(90);
        assertEquals("1m 30s", TimeUtils.convertToReadableTime(sec90));
        BigDecimal sec120 = BigDecimal.valueOf(120);
        assertEquals("2m", TimeUtils.convertToReadableTime(sec120));
        BigDecimal sec3600 = BigDecimal.valueOf(3600);
        assertEquals("1h", TimeUtils.convertToReadableTime(sec3600));
        BigDecimal sec3620 = BigDecimal.valueOf(3620);
        assertEquals("1h 20s", TimeUtils.convertToReadableTime(sec3620));
        BigDecimal sec3665 = BigDecimal.valueOf(3665);
        assertEquals("1h 1m 5s", TimeUtils.convertToReadableTime(sec3665));
    }

}
