package com.mined.in.description;

import com.tverdokhlebd.coin.market.CoinMarketType;

/**
 * Enumerations of supporting coin markets.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinMarketDescription {

    COIN_MARKET_CAP("CoinMarketCap", "https://coinmarketcap.com", CoinMarketType.COIN_MARKET_CAP);

    /** Name of coin market. */
    private String name;
    /** Official site of coin market. */
    private String website;
    /** Type of coin market. */
    private CoinMarketType coinMarketType;

    /**
     * Creates instance.
     *
     * @param name name of coin market
     * @param website official site of coin market
     * @param coinMarketType type of coin market
     */
    private CoinMarketDescription(String name, String website, CoinMarketType coinMarketType) {
        this.name = name;
        this.website = website;
        this.coinMarketType = coinMarketType;
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
     * Gets coin market type.
     *
     * @return coin market type
     */
    public CoinMarketType getCoinMarketType() {
        return coinMarketType;
    }

}
