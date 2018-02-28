package com.mined.in.coin;

/**
 * Enumeration of supporting coins.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum Coin {

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
    private Coin(String name, String symbol, String website) {
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
    public static Coin getBySymbol(String symbol) {
        Coin[] coinArray = Coin.values();
        for (int i = 0; i < coinArray.length; i++) {
            Coin coin = coinArray[i];
            if (coin.getSymbol().equals(symbol)) {
                return coin;
            }
        }
        throw new IllegalArgumentException("No coins with symbol " + symbol);
    }

}
