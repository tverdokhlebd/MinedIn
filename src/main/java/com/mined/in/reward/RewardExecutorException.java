package com.mined.in.reward;

import com.mined.in.http.ErrorCode;
import com.mined.in.http.RequestException;

/**
 * Exception for working with estimated reward executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RewardExecutorException extends RequestException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4078780829401257645L;

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public RewardExecutorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public RewardExecutorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
