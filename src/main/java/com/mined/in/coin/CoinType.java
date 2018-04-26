package com.mined.in.coin;

import com.mined.in.coin.wallet.AddressValidator;
import com.mined.in.coin.wallet.BitcoinAddressValidator;
import com.mined.in.coin.wallet.EthereumAddressValidator;
import com.mined.in.coin.wallet.MoneroAddressValidator;

/**
 * Enumerations of supporting coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    BTC("Bitcoin", "BTC", "https://bitcoin.org", false, new BitcoinAddressValidator()),
    ETH("Ethereum", "ETH", "https://www.ethereum.org", true, new EthereumAddressValidator()),
    ETC("Ethereum Classic", "ETC", "https://ethereumclassic.github.io", false, new EthereumAddressValidator()),
    XMR("Monero", "XMR", "https://getmonero.org", true, new MoneroAddressValidator()),
    ZEC("Zcash", "ZEC", "https://z.cash", false, new MoneroAddressValidator());

    /** Coin type name. */
    private String name;
    /** Coin type symbol. */
    private String symbol;
    /** Coin type official site. */
    private String website;
    /** Enabled for mining statistics or not. */
    private boolean enabled;
    /** Address validator. */
    private AddressValidator addressValidator;

    /**
     * Creates the instance.
     *
     * @param name coin type name
     * @param symbol coin type symbol
     * @param website coin type official site
     * @param enabled enabled for mining statistics or not
     * @param addressValidator address validator
     */
    private CoinType(String name, String symbol, String website, boolean enabled, AddressValidator addressValidator) {
        this.name = name;
        this.symbol = symbol;
        this.website = website;
        this.enabled = enabled;
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
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
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
