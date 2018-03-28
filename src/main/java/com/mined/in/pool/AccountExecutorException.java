package com.mined.in.pool;

import com.mined.in.error.ErrorCode;

/**
 * Exception for working with pool account executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountExecutorException extends Exception {

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
    public AccountExecutorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates the exception instance for working with pool account executor.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public AccountExecutorException(ErrorCode errorCode, Throwable cause) {
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
