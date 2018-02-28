package com.mined.in.exchanger.currencypair;

/**
 * Exception for working with currency pair of exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CurrencyPairException extends Exception {

    private static final long serialVersionUID = -1553863222669549323L;

    /**
     * Creates the exception instance for working with currency pair of exchanger.
     *
     * @param cause the cause
     */
    public CurrencyPairException(Throwable cause) {
        super(cause);
    }

}
