package com.mined.in.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Utils for working with task.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TaskUtils {

    /**
     * Starts repeated task.
     *
     * @param taskName task name
     * @param task task that can be scheduled
     * @param period time in milliseconds between successive task executions
     */
    public static void startRepeatedTask(String taskName, TimerTask task, long period) {
        Timer timer = new Timer(taskName, true);
        timer.scheduleAtFixedRate(task, period, period);
    }

}
