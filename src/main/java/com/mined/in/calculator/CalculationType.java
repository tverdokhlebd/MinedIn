package com.mined.in.calculator;

/**
 * Enumerations of supporting mining calculations.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CalculationType {

    WHAT_TO_MINE("WhatToMine", "https://whattomine.com");

    /** Calculation name. */
    private String name;
    /** Calculation official site. */
    private String website;

    /**
     * Creates the instance of mining calculation.
     *
     * @param name calculation name
     * @param website calculation official site
     */
    private CalculationType(String name, String website) {
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
     * Returns mining calculation by name.
     *
     * @param name calculation name
     * @return mining calculation
     */
    public static CalculationType getByName(String name) {
        CalculationType[] calculationTypeArray = CalculationType.values();
        for (int i = 0; i < calculationTypeArray.length; i++) {
            CalculationType calculationType = calculationTypeArray[i];
            if (calculationType.getName().equals(name)) {
                return calculationType;
            }
        }
        throw new IllegalArgumentException("No mining calculation with name " + name);
    }

}
