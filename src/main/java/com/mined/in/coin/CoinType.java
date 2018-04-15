package com.mined.in.coin;

import com.mined.in.coin.wallet.AddressValidator;
import com.mined.in.coin.wallet.EthereumAddressValidator;
import com.mined.in.coin.wallet.ZCashAddressValidator;

/**
 * Enumerations of supporting coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    ETH("Ethereum", "ETH", "https://www.ethereum.org", new EthereumAddressValidator()),
    ETC("Ethereum Classic", "ETC", "https://ethereumclassic.github.io/", new EthereumAddressValidator()),
    ZEC("Zcash", "ZEC", "https://z.cash/", new ZCashAddressValidator());

    /** Coin type name. */
    private String name;
    /** Coin type symbol. */
    private String symbol;
    /** Coin type official site. */
    private String website;
    /** Address validator. */
    private AddressValidator addressValidator;

    /**
     * Creates the instance.
     *
     * @param name coin type name
     * @param symbol coin type symbol
     * @param website coin type official site
     * @param addressValidator address validator
     */
    private CoinType(String name, String symbol, String website, AddressValidator addressValidator) {
        this.name = name;
        this.symbol = symbol;
        this.website = website;
        this.addressValidator = addressValidator;
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
     * Gets the address validator.
     *
     * @return the address validator
     */
    public AddressValidator getAddressValidator() {
        return addressValidator;
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
