package com.mined.in.api.web;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Web response with custom format.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of data
 */
public class WebResponse<T> {

    /** HTTP status. */
    private HttpStatus status;
    /** Response data. */
    private T data;
    /** Error message. */
    private String error;

    /**
     * Creates the instance.
     */
    public WebResponse() {
        super();
    }

    /**
     * Creates the instance.
     *
     * @param data response data
     */
    public WebResponse(T data) {
        this.data = data;
    }

    /**
     * Creates response entity with successful status.
     *
     * @return response entity with successful status
     */
    public ResponseEntity<WebResponse<T>> create() {
        this.status = HttpStatus.OK;
        return new ResponseEntity<WebResponse<T>>(this, status);
    }

    /**
     * Creates response entity with unsuccessful status.
     *
     * @param status HTTP status
     * @param error error message
     * @return response entity with unsuccessful status
     */
    public ResponseEntity<WebResponse<T>> create(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
        return new ResponseEntity<WebResponse<T>>(this, status);
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
        return this.status == OK;
    }

}
