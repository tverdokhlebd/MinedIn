package com.mined.in.exchanger.pair;

import com.mined.in.error.ErrorCode;

/**
 * Exception for working with pair executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class PairExecutorException extends Exception {

    /** Error code. */
    private final ErrorCode errorCode;
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -448307664169612373L;

    /**
     * Creates the exception instance for working with pool account executor.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public PairExecutorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates the exception instance for working with pool account executor.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public PairExecutorException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
