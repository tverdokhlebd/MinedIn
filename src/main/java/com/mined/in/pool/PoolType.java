package com.mined.in.pool;

/**
 * Enumerations of supporting pools.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolType {

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com"),
    ETHERMINE("Ethermine", "https://ethermine.org"),
    NANOPOOL("Nanopool", "https://nanopool.org");

    /** Pool name. */
    private String name;
    /** Pool official site. */
    private String website;

    /**
     * Creates the instance of supporting pool.
     *
     * @param name pool name
     * @param website pool official site
     */
    private PoolType(String name, String website) {
        this.name = name;
        this.website = website;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the website.
     *
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Returns pool by name.
     *
     * @param name pool name
     * @return pool
     */
    public static PoolType getByName(String name) {
        PoolType[] poolArray = PoolType.values();
        for (int i = 0; i < poolArray.length; i++) {
            PoolType pool = poolArray[i];
            if (pool.getName().equals(name)) {
                return pool;
            }
        }
        throw new IllegalArgumentException("No pools with name " + name);
    }

}
