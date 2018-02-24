package com.mining.profit.bot;

/**
 * Exception for working with bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BotException extends Exception {

    private static final long serialVersionUID = -1606597777837002997L;

    /**
     * Creates the exception instance for working with bot.
     *
     * @param cause the cause
     */
    public BotException(String message) {
        super(message);
    }

}
