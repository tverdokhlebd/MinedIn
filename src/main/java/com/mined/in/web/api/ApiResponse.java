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

    /** HTTP status. */
    private HttpStatus status;
    /** Response data. */
    private T data;
    /** Error message. */
    private String error;

    /**
     * Creates the instance.
     */
    public ApiResponse() {
        super();
    }

    /**
     * Creates the instance.
     *
     * @param data response data
     */
    public ApiResponse(T data) {
        this.data = data;
    }

    /**
     * Creates response entity with data.
     *
     * @return response entity with data
     */
    public ResponseEntity<ApiResponse<T>> createSuccess() {
        this.status = HttpStatus.OK;
        return new ResponseEntity<ApiResponse<T>>(this, status);
    }

    /**
     * Creates response entity with error.
     *
     * @param error error message
     * @return response entity with error
     */
    public ResponseEntity<ApiResponse<T>> createError(String error) {
        this.status = HttpStatus.OK;
        this.error = error;
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
     * Gets the error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public boolean getStatus() {
        return this.error == null || this.error.isEmpty();
    }

}
