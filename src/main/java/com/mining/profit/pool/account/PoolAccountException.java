package com.mining.profit.pool.account;

/**
 * Exception for working with pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class PoolAccountException extends Exception {

    private static final long serialVersionUID = -1553863222669549323L;

    /**
     * Creates the exception instance for working with pool account.
     *
     * @param cause the cause
     */
    public PoolAccountException(Throwable cause) {
        super(cause);
    }

}
