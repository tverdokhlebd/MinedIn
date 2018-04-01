package com.mined.in.earnings.worker;

import java.math.BigDecimal;

import com.mined.in.coin.CoinMarket;
import com.mined.in.earnings.Earnings;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;

/**
 * Worker for calculating ethereum earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class EthereumWorker implements EarningsWorker {

    /** Pool account requestor. */
    private final AccountRequestor accountRequestor;
    /** Market requestor. */
    private final MarketRequestor marketRequestor;
    /** Estimated reward requestor. */
    private final RewardRequestor rewardRequestor;

    /**
     * Creates the instance.
     *
     * @param accountRequestor pool account requestor
     * @param marketRequestor market requestor
     * @param rewardRequestor estimated reward requestor
     */
    EthereumWorker(AccountRequestor accountRequestor, MarketRequestor marketRequestor, RewardRequestor rewardRequestor) {
        super();
        this.accountRequestor = accountRequestor;
        this.marketRequestor = marketRequestor;
        this.rewardRequestor = rewardRequestor;
    }

    @Override
    public Earnings calculate(String walletAddress)
            throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        Account account = accountRequestor.requestEthereumAccount(walletAddress);
        BigDecimal walletBalance = account.getWalletBalance();
        CoinMarket coinMarket = marketRequestor.requestEthereumCoin();
        BigDecimal coinPrice = coinMarket.getPrice();
        BigDecimal balanceInUSD = walletBalance.multiply(coinPrice);
        Reward reward = rewardRequestor.requestEthereumReward(account.getTotalHashrate());
        return new Earnings(walletBalance, balanceInUSD, coinPrice, reward);
    }

}
