package com.mined.in.pool;

import com.mined.in.http.ErrorCode;
import com.mined.in.http.RequestException;

/**
 * Exception for working with pool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountRequestorException extends RequestException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2187968069795783438L;

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public AccountRequestorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public AccountRequestorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
