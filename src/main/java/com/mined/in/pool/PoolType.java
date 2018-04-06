package com.mined.in.pool;

import static com.mined.in.coin.CoinType.ETH;

import java.util.Arrays;
import java.util.List;

import com.mined.in.coin.CoinType;

/**
 * Enumerations of supporting pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolType {

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com", Arrays.asList(ETH)),
    ETHERMINE("Ethermine", "https://ethermine.org", Arrays.asList(ETH)),
    NANOPOOL("Nanopool", "https://nanopool.org", Arrays.asList(ETH));

    /** Pool type name. */
    private String name;
    /** Pool type official site. */
    private String website;
    /** Supported list of coin types. */
    private List<CoinType> coinTypeList;

    /**
     * Creates the instance.
     *
     * @param name pool type name
     * @param website pool type official site
     * @param coinTypeList supported list of coin types
     */
    private PoolType(String name, String website, List<CoinType> coinTypeList) {
        this.name = name;
        this.website = website;
        this.coinTypeList = coinTypeList;
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
     * Gets the coin type list.
     *
     * @return the coin type list
     */
    public List<CoinType> getCoinTypeList() {
        return coinTypeList;
    }

    /**
     * Gets pool type by name.
     *
     * @param name pool type name
     * @return pool type
     */
    public static PoolType getByName(String name) {
        PoolType[] poolTypeArray = PoolType.values();
        for (int i = 0; i < poolTypeArray.length; i++) {
            PoolType poolType = poolTypeArray[i];
            if (poolType.getName().equals(name)) {
                return poolType;
            }
        }
        throw new IllegalArgumentException("No pool type with name " + name);
    }

}
