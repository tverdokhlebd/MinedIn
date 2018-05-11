package com.mined.in.description;

import com.tverdokhlebd.mining.pool.PoolType;

/**
 * Enumerations of supporting pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolTypeDescription {

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com", PoolType.DWARFPOOL),
    ETHERMINE("Ethermine", "https://ethermine.org", PoolType.ETHERMINE),
    NANOPOOL("Nanopool", "https://nanopool.org", PoolType.NANOPOOL);

    /** Name of pool type. */
    private String name;
    /** Official site of pool type. */
    private String website;
    /** Type of pool. */
    private PoolType poolType;

    /**
     * Creates instance.
     *
     * @param name name of pool type
     * @param website official site of pool type
     * @param poolType type of pool
     */
    private PoolTypeDescription(String name, String website, PoolType poolType) {
        this.name = name;
        this.website = website;
        this.poolType = poolType;
    }

    /**
     * Gets name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets website.
     *
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Gets pool type.
     *
     * @return pool type
     */
    public PoolType getPoolType() {
        return poolType;
    }

}
