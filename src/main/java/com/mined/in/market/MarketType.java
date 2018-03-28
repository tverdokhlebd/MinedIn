package com.mined.in.market;

/**
 * Enumerations of supporting market types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum MarketType {

    COIN_MARKET_CAP("CoinMarketCap", "https://coinmarketcap.com");

    /** Market type name. */
    private String name;
    /** Market type official site. */
    private String website;

    /**
     * Creates the instance.
     *
     * @param name market type name
     * @param website market type official site
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
     * Gets market type by name.
     *
     * @param name market type name
     * @return market type
     */
    public static MarketType getByName(String name) {
        MarketType[] marketTypeArray = MarketType.values();
        for (int i = 0; i < marketTypeArray.length; i++) {
            MarketType marketType = marketTypeArray[i];
            if (marketType.getName().equals(name)) {
                return marketType;
            }
        }
        throw new IllegalArgumentException("No market type with name " + name);
    }

}
