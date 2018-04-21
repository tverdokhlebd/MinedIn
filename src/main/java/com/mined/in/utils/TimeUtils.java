package com.mined.in.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Utils for working with time.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TimeUtils {

    /** Hours in day. */
    public static final BigDecimal HOURS_IN_DAY = BigDecimal.valueOf(24);
    /** Days in week. */
    public static final BigDecimal DAYS_IN_WEEK = BigDecimal.valueOf(7);
    /** Days in month. */
    public static final BigDecimal DAYS_IN_MONTH = BigDecimal.valueOf(30);
    /** Days in year. */
    public static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);
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

    /**
     * Adds minutes to date.
     *
     * @param currentDate current date
     * @param amount amount of minutes
     * @return date with added amount of minutes
     */
    public static Date addMinutes(Date currentDate, int amount) {
        Calendar now = Calendar.getInstance();
        now.setTime(currentDate);
        now.add(Calendar.MINUTE, amount);
        return now.getTime();
    }

}
