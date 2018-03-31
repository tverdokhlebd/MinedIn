package com.mined.in.pool;

import com.mined.in.http.ErrorCode;
import com.mined.in.http.RequestException;

/**
 * Exception for working with pool account executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountExecutorException extends RequestException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2187968069795783438L;

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public AccountExecutorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public AccountExecutorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
