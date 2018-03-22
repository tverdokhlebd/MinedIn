package com.mined.in.market;

/**
 * Enumerations of supporting markets.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum MarketType {

    COIN_MARKET_CAP("CoinMarketCap", "https://coinmarketcap.com");

    /** Market name. */
    private String name;
    /** Market official site. */
    private String website;

    /**
     * Creates the instance of supporting market.
     *
     * @param name market name
     * @param website market official site
     */
    private MarketType(String name, String website) {
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
     * Returns market by name.
     *
     * @param name market name
     * @return market
     */
    public static MarketType getByName(String name) {
        MarketType[] marketTypeArray = MarketType.values();
        for (int i = 0; i < marketTypeArray.length; i++) {
            MarketType marketType = marketTypeArray[i];
            if (marketType.getName().equals(name)) {
                return marketType;
            }
        }
        throw new IllegalArgumentException("No market with name " + name);
    }

}
