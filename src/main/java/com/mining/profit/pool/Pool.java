package com.mining.profit.pool;

import java.math.BigDecimal;

/**
 * Interface for representing of pool information like hashrate, balance and etc.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface Pool {

    /**
     * Returns the wallet address.
     *
     * @return the wallet address
     */
    String getWalletAddress();

    /**
     * Returns the wallet balance.
     *
     * @return the wallet balance
     */
    BigDecimal getWalletBalance();

}
