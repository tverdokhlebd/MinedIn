package com.mined.in.pool;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;

/**
 * Interface for account caching.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountCaching {

    /**
     * Gets cached account map.
     *
     * @return cached account map
     */
    Map<String, SimpleEntry<Account, Date>> getCachedAccountMap();

}
