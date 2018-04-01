package com.mined.in.market;

import com.mined.in.http.ErrorCode;
import com.mined.in.http.RequestException;

/**
 * Exception for working with market requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MarketRequestorException extends RequestException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3067598387432040867L;

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public MarketRequestorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public MarketRequestorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
