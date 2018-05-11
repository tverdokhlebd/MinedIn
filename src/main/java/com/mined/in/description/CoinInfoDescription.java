package com.mined.in.description;

import com.tverdokhlebd.coin.info.CoinInfoType;

/**
 * Enumerations of supporting coin info.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinInfoDescription {

    WHAT_TO_MINE("WhatToMine", "https://whattomine.com", CoinInfoType.WHAT_TO_MINE);

    /** Name of coin info. */
    private String name;
    /** Official site of coin info. */
    private String website;
    /** Type of coin info. */
    private CoinInfoType coinInfoType;

    /**
     * Creates instance.
     *
     * @param name name of coin info
     * @param website official site of coin info
     * @param coinInfoType type of coin info
     */
    private CoinInfoDescription(String name, String website, CoinInfoType coinInfoType) {
        this.name = name;
        this.website = website;
        this.coinInfoType = coinInfoType;
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
     * Gets coin info type.
     *
     * @return coin info type
     */
    public CoinInfoType getCoinInfoType() {
        return coinInfoType;
    }

}
