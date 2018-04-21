package com.mined.in.http;

/**
 * Base requestor for HTTP.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of the argument
 * @param <R> type of the result
 */
public interface BaseRequestor<T, R> {

    /**
     * Gets url.
     *
     * @return url
     */
    String getUrl();

    /**
     * Makes request.
     *
     * @return response
     * @throws Exception if there is any error in requesting
     */
    R request() throws Exception;

    /**
     * Makes request.
     *
     * @param t data
     * @return response
     * @throws Exception if there is any error in requesting
     */
    R request(T t) throws Exception;

}
