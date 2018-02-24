package com.mining.profit.bot;

/**
 * Interface for processing of bot request.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface Bot {

    /**
     * Processes bot request.
     *
     * @param request bot request
     */
    void process(String request);

}
