package com.mined.in.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * API response with custom format.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of data
 */
public class ApiResponse<T> {

    /**
     * Enumerations of error types.
     */
    public static enum ErrorType {

        COMMON,
        POOL,
        MARKET,
        REWARD

    }

    /** HTTP status. */
    private HttpStatus status;
    /** Response data. */
    private T data;
    /** Error flag. */
    private boolean error;
    /** Error type. */
    private ErrorType errorType;
    /** Error message. */
    private String errorMessage;

    /**
     * Creates the instance.
     */
    public ApiResponse() {
        super();
        this.status = HttpStatus.OK;
    }

    /**
     * Creates the instance.
     *
     * @param data response data
     */
    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Creates response entity with data.
     *
     * @return response entity with data
     */
    public ResponseEntity<ApiResponse<T>> create() {
        this.error = false;
        return new ResponseEntity<ApiResponse<T>>(this, status);
    }

    /**
     * Creates response entity with error.
     *
     * @return response entity with error
     */
    public ResponseEntity<ApiResponse<T>> create(ErrorType errorType, String errorMessage) {
        this.error = true;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        return new ResponseEntity<ApiResponse<T>>(this, status);
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Checks if is error.
     *
     * @return true, if is error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Gets the error type.
     *
     * @return the error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
