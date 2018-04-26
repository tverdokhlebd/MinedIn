package com.mined.in.coin;

/**
 * Enumerations of supporting coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    BTC("Bitcoin", "BTC", "https://bitcoin.org", false),
    ETH("Ethereum", "ETH", "https://www.ethereum.org", true),
    ETC("Ethereum Classic", "ETC", "https://ethereumclassic.github.io", true),
    XMR("Monero", "XMR", "https://getmonero.org", true),
    ZEC("Zcash", "ZEC", "https://z.cash", true);

    /** Coin type name. */
    private String name;
    /** Coin type symbol. */
    private String symbol;
    /** Coin type official site. */
    private String website;
    /** Enabled for mining statistics or not. */
    private boolean enabled;

    /**
     * Creates the instance.
     *
     * @param name coin type name
     * @param symbol coin type symbol
     * @param website coin type official site
     * @param enabled enabled for mining statistics or not
     */
    private CoinType(String name, String symbol, String website, boolean enabled) {
        this.name = name;
        this.symbol = symbol;
        this.website = website;
        this.enabled = enabled;
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
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
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
