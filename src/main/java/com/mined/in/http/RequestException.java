package com.mined.in.http;

/**
 * Exception for working with HTTP requests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RequestException extends Exception {

    /** Error code. */
    private final ErrorCode errorCode;
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4546411797257311520L;

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param message the detail message
     */
    public RequestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates the instance.
     *
     * @param errorCode error code
     * @param cause the cause
     */
    public RequestException(ErrorCode errorCode, Throwable cause) {
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
