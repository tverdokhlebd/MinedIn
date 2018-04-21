package com.mined.in.pool;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;

import com.mined.in.http.BaseRequestor;

/**
 * Base account requestor.
 * 
 * @author Dmitry Tverdokhleb
 *
 * @param <T> type of the argument
 * @param <R> type of the result
 */
public interface BaseAccountRequestor<T, R> extends BaseRequestor<T, R> {

    /**
     * Gets cached account map.
     *
     * @return cached account map
     */
    Map<String, SimpleEntry<Account, Date>> getCachedAccountMap();

}
