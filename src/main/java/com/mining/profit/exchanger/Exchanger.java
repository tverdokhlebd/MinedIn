package com.mining.profit.exchanger;

/**
 * Enumeration of supporting exchangers.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum Exchanger {

    EXMO("Exmo", "https://exmo.com/");

    /** Exchanger name. */
    private String name;
    /** Exchanger official site. */
    private String website;

    /**
     * Creates the instance of supporting exchanger.
     *
     * @param name exchanger name
     * @param website exchanger official site
     */
    private Exchanger(String name, String website) {
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
     * Returns exchanger by name.
     *
     * @param name exchanger name
     * @return exchanger
     */
    public static Exchanger getByName(String name) {
        Exchanger[] exchangerArray = Exchanger.values();
        for (int i = 0; i < exchangerArray.length; i++) {
            Exchanger exchanger = exchangerArray[i];
            if (exchanger.getName().equals(name)) {
                return exchanger;
            }
        }
        throw new IllegalArgumentException("No exchangers with name " + name);
    }

}
