package com.mined.in.description;

import com.tverdokhlebd.mining.commons.coin.CoinType;

/**
 * Enumerations of supporting coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinTypeDescription {

    BTC("Bitcoin", "https://bitcoin.org", false, CoinType.BTC),
    ETH("Ethereum", "https://www.ethereum.org", true, CoinType.ETH),
    ETC("Ethereum Classic", "https://ethereumclassic.github.io", true, CoinType.ETC),
    XMR("Monero", "https://getmonero.org", true, CoinType.XMR),
    ZEC("Zcash", "https://z.cash", true, CoinType.ZEC);

    /** Name of coin type. */
    private String name;
    /** Official site of coin type. */
    private String website;
    /** Enabled for mining statistics or not. */
    private boolean enabled;
    /** Type of coin. */
    private CoinType coinType;

    /**
     * Creates instance.
     *
     * @param name name of coin type
     * @param website official site of coin type
     * @param enabled enabled for mining statistics or not
     * @param coinType type of coin
     */
    private CoinTypeDescription(String name, String website, boolean enabled, CoinType coinType) {
        this.name = name;
        this.website = website;
        this.enabled = enabled;
        this.coinType = coinType;
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
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets coin type.
     *
     * @return coin type
     */
    public CoinType getCoinType() {
        return coinType;
    }

}
