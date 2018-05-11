package com.mined.in.description;

import com.tverdokhlebd.coin.reward.CoinRewardType;

/**
 * Enumerations of supporting coin rewards.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinRewardDescription {

    WHAT_TO_MINE("WhatToMine", "https://whattomine.com", CoinRewardType.WHAT_TO_MINE);

    /** Name of coin reward. */
    private String name;
    /** Official site of coin reward. */
    private String website;
    /** Type of coin reward. */
    private CoinRewardType coinRewardType;

    /**
     * Creates instance.
     *
     * @param name name of coin reward
     * @param website official site of coin reward
     * @param coinRewardType type of coin reward
     */
    private CoinRewardDescription(String name, String website, CoinRewardType coinRewardType) {
        this.name = name;
        this.website = website;
        this.coinRewardType = coinRewardType;
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
     * Gets coin reward type.
     *
     * @return coin reward type
     */
    public CoinRewardType getCoinRewardType() {
        return coinRewardType;
    }

}
