package com.mining.profit.pool.account;

/**
 * Exception for working with pool account executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class PoolAccountExecutorException extends Exception {

    private static final long serialVersionUID = -448307664169612373L;

    /**
     * Creates the exception instance for working with pool account executor.
     *
     * @param cause the cause
     */
    public PoolAccountExecutorException(Throwable cause) {
        super(cause);
    }

}
