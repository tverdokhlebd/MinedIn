package com.mined.in.utils;

import java.math.BigDecimal;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Converting time to readable time format.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ReadableTimeUtil {

    /** Text resources. */
    private final static ResourceBundle RESOURCES = ResourceBundle.getBundle(ReadableTimeUtil.class.getName());

    /**
     * Converts time to readable time format.
     *
     * @param time time in seconds
     * @return readable time format
     */
    public static String convertToReadableTime(BigDecimal time) {
        long duration = time.longValue();
        long hours = TimeUnit.SECONDS.toHours(duration);
        duration -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(duration);
        duration -= TimeUnit.MINUTES.toSeconds(minutes);
        long seconds = TimeUnit.SECONDS.toSeconds(duration);
        StringBuilder readableTime = new StringBuilder();
        if (hours != 0) {
            readableTime.append(hours + RESOURCES.getString("hour"));
        }
        if (minutes != 0) {
            readableTime.append((readableTime.length() > 0 ? " " : "") + minutes + RESOURCES.getString("minute"));
        }
        if (seconds != 0) {
            readableTime.append((readableTime.length() > 0 ? " " : "") + seconds + RESOURCES.getString("second"));
        }
        return readableTime.toString();
    }

}
