package com.mined.in.reward;

/**
 * Enumerations of supporting reward types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum RewardType {

    WHAT_TO_MINE("WhatToMine", "https://whattomine.com");

    /** Reward type name. */
    private String name;
    /** Reward type official site. */
    private String website;

    /**
     * Creates the instance.
     *
     * @param name reward type name
     * @param website reward type official site
     */
    private RewardType(String name, String website) {
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
     * Gets reward type by name.
     *
     * @param name reward type name
     * @return reward type
     */
    public static RewardType getByName(String name) {
        RewardType[] rewardTypeArray = RewardType.values();
        for (int i = 0; i < rewardTypeArray.length; i++) {
            RewardType rewardType = rewardTypeArray[i];
            if (rewardType.getName().equals(name)) {
                return rewardType;
            }
        }
        throw new IllegalArgumentException("No reward type with name " + name);
    }

}
