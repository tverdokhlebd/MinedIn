package com.mined.in.pool;

/**
 * Enumerations of supporting pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolType {

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com"),
    ETHERMINE("Ethermine", "https://ethermine.org"),
    NANOPOOL("Nanopool", "https://nanopool.org");

    /** Pool type name. */
    private String name;
    /** Pool type official site. */
    private String website;

    /**
     * Creates the instance.
     *
     * @param name pool type name
     * @param website pool type official site
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
     * Gets pool type by name.
     *
     * @param name pool type name
     * @return pool type
     */
    public static PoolType getByName(String name) {
        PoolType[] poolTypeArray = PoolType.values();
        for (int i = 0; i < poolTypeArray.length; i++) {
            PoolType poolType = poolTypeArray[i];
            if (poolType.getName().equals(name)) {
                return poolType;
            }
        }
        throw new IllegalArgumentException("No pool type with name " + name);
    }

}
