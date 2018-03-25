package com.mined.in.calculator;

import com.mined.in.error.ErrorCode;

/**
 * Exception for working with mining calculation executor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class CalculationExecutorException extends Exception {

    /** Error code. */
    private final ErrorCode errorCode;
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4546411797257311520L;

    /**
     * Creates the exception instance for working with mining calculation executor.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public CalculationExecutorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates the exception instance for working with mining calculation executor.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public CalculationExecutorException(ErrorCode errorCode, Throwable cause) {
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
