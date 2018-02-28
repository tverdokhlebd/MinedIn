package com.mined.in.exchanger.currencypair;

/**
 * Exception for working with currency pair executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CurrencyPairExecutorException extends Exception {

    /** HTTP status code. */
    private int code;

    private static final long serialVersionUID = -448307664169612373L;

    /**
     * Creates the exception instance for working with currency pair executor.
     *
     * @param cause the cause
     */
    public CurrencyPairExecutorException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates the exception instance for working with currency pair executor.
     *
     * @param code HTTP status code
     */
    public CurrencyPairExecutorException(int code) {
        super();
        this.code = code;
    }

}
