package com.mined.in.pool;

/**
 * Enumeration of supporting pools.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum Pool {

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com/"),
    ETHERMINE("Ethermine", "https://ethermine.org/");

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
    private Pool(String name, String website) {
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
    public static Pool getByName(String name) {
        Pool[] poolArray = Pool.values();
        for (int i = 0; i < poolArray.length; i++) {
            Pool pool = poolArray[i];
            if (pool.getName().equals(name)) {
                return pool;
            }
        }
        throw new IllegalArgumentException("No pools with name " + name);
    }

}
