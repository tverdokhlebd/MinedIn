package com.mined.in.reward;

/**
 * Enumerations of supporting rewards.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum RewardType {

    WHAT_TO_MINE("WhatToMine", "https://whattomine.com");

    /** Reward name. */
    private String name;
    /** Reward official site. */
    private String website;

    /**
     * Creates the instance of reward.
     *
     * @param name reward name
     * @param website reward official site
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
     * Returns reward by name.
     *
     * @param name reward name
     * @return reward
     */
    public static RewardType getByName(String name) {
        RewardType[] rewardTypeArray = RewardType.values();
        for (int i = 0; i < rewardTypeArray.length; i++) {
            RewardType rewardType = rewardTypeArray[i];
            if (rewardType.getName().equals(name)) {
                return rewardType;
            }
        }
        throw new IllegalArgumentException("No reward with name " + name);
    }

}
