package com.tverdokhlebd.minedin.bot;

/**
 * Interface for processing incoming updates from bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface BotUpdates {

    /**
     * Processes incoming updates from bot.
     *
     * @param updates incoming updates
     */
    void process(String updates);

}
