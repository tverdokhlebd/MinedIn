package com.mined.in.earnings;

import java.math.BigDecimal;

import com.tverdokhlebd.coin.info.CoinInfo;
import com.tverdokhlebd.coin.market.CoinMarket;
import com.tverdokhlebd.coin.reward.CoinReward;
import com.tverdokhlebd.mining.pool.Account;

/**
 * Calculated earnings. It uses as final result of calculations.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Earnings {

    /** Balance in USD. */
    private final BigDecimal usdBalance;
    /** Account. */
    private final Account account;
    /** Coin info. */
    private final CoinInfo coinInfo;
    /** Coin market. */
    private final CoinMarket coinMarket;
    /** Coin reward. */
    private final CoinReward coinReward;

    /**
     * Creates instance.
     *
     * @param usdBalance balance in USD
     * @param account account
     * @param coinInfo coin info
     * @param coinMarket coin market
     * @param coinReward coin reward
     */
    public Earnings(BigDecimal usdBalance, Account account, CoinInfo coinInfo, CoinMarket coinMarket, CoinReward coinReward) {
        super();
        this.usdBalance = usdBalance;
        this.account = account;
        this.coinInfo = coinInfo;
        this.coinMarket = coinMarket;
        this.coinReward = coinReward;
    }

    /**
     * Gets USD balance.
     *
     * @return USD balance
     */
    public BigDecimal getUsdBalance() {
        return usdBalance;
    }

    /**
     * Gets account.
     *
     * @return account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Gets coin info.
     *
     * @return coin info
     */
    public CoinInfo getCoinInfo() {
        return coinInfo;
    }

    /**
     * Gets coin market.
     *
     * @return coin market
     */
    public CoinMarket getCoinMarket() {
        return coinMarket;
    }

    /**
     * Gets coin reward.
     *
     * @return coin reward
     */
    public CoinReward getCoinReward() {
        return coinReward;
    }

}
