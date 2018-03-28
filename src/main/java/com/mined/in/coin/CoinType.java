package com.mined.in.coin;

/**
 * Enumerations of supporting coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    ETH("Ethereum", "ETH", "https://www.ethereum.org/");

    /** Coin type name. */
    private String name;
    /** Coin type symbol. */
    private String symbol;
    /** Coin type official site. */
    private String website;

    /**
     * Creates the instance.
     *
     * @param name coin type name
     * @param symbol coin type symbol
     * @param website coin type official site
     */
    private CoinType(String name, String symbol, String website) {
        this.name = name;
        this.symbol = symbol;
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
     * Gets the symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
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
     * Gets coin type by symbol.
     *
     * @param symbol coin type symbol
     * @return coin type
     */
    public static CoinType getBySymbol(String symbol) {
        CoinType[] coinTypeArray = CoinType.values();
        for (int i = 0; i < coinTypeArray.length; i++) {
            CoinType coinType = coinTypeArray[i];
            if (coinType.getSymbol().equals(symbol)) {
                return coinType;
            }
        }
        throw new IllegalArgumentException("No coin type with symbol " + symbol);
    }

}
