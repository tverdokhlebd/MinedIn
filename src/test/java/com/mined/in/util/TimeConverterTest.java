package com.mined.in.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mined.in.utils.TimeConverter;

/**
 * Tests of time converter.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class TimeConverterTest {

    @Test
    public void testConvertingToTime() {
        BigDecimal sec30 = BigDecimal.valueOf(30.33);
        assertEquals("30s", TimeConverter.convertToReadableTime(sec30));
        BigDecimal sec60 = BigDecimal.valueOf(60.33);
        assertEquals("1m", TimeConverter.convertToReadableTime(sec60));
        BigDecimal sec75 = BigDecimal.valueOf(75);
        assertEquals("1m 15s", TimeConverter.convertToReadableTime(sec75));
        BigDecimal sec90 = BigDecimal.valueOf(90);
        assertEquals("1m 30s", TimeConverter.convertToReadableTime(sec90));
        BigDecimal sec120 = BigDecimal.valueOf(120);
        assertEquals("2m", TimeConverter.convertToReadableTime(sec120));
        BigDecimal sec3600 = BigDecimal.valueOf(3600);
        assertEquals("1h", TimeConverter.convertToReadableTime(sec3600));
        BigDecimal sec3620 = BigDecimal.valueOf(3620);
        assertEquals("1h 20s", TimeConverter.convertToReadableTime(sec3620));
        BigDecimal sec3665 = BigDecimal.valueOf(3665);
        assertEquals("1h 1m 5s", TimeConverter.convertToReadableTime(sec3665));
    }

}
