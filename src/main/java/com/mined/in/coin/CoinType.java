package com.mined.in.coin;

/**
 * Enumerations of supporting coins.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    ETH("Ethereum", "ETH", "https://www.ethereum.org/");

    /** Coin name. */
    private String name;
    /** Coin symbol. */
    private String symbol;
    /** Coin official site. */
    private String website;

    /**
     * Creates the instance of supporting coin.
     *
     * @param name coin name
     * @param symbol coin symbol
     * @param website coin official site
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
     * Returns coin by symbol.
     *
     * @param symbol coin symbol
     * @return coin
     */
    public static CoinType getBySymbol(String symbol) {
        CoinType[] coinArray = CoinType.values();
        for (int i = 0; i < coinArray.length; i++) {
            CoinType coin = coinArray[i];
            if (coin.getSymbol().equals(symbol)) {
                return coin;
            }
        }
        throw new IllegalArgumentException("No coins with symbol " + symbol);
    }

}
