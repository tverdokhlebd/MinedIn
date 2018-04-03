package com.mined.in.utils;

import java.math.BigDecimal;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Utils for working with time.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TimeConverter {

    /** Text resources. */
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("text");

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
            readableTime.append(hours + RESOURCE.getString("hour"));
        }
        if (minutes != 0) {
            readableTime.append((readableTime.length() > 0 ? " " : "") + minutes + RESOURCE.getString("minute"));
        }
        if (seconds != 0) {
            readableTime.append((readableTime.length() > 0 ? " " : "") + seconds + RESOURCE.getString("second"));
        }
        return readableTime.toString();
    }

}
