package com.mined.in.bot;

/**
 * Interface for processing of incoming updates from a bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface BotUpdates {

    /**
     * Processes incoming updates from a bot.
     *
     * @param updates incoming updates
     */
    void process(String updates);

}
