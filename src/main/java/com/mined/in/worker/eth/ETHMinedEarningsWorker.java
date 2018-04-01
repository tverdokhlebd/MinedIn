package com.mined.in.worker.eth;

import java.math.BigDecimal;

import com.mined.in.coin.CoinMarket;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.worker.MinedEarnings;
import com.mined.in.worker.MinedEarningsWorker;

/**
 * Worker for calculating ETH mined earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHMinedEarningsWorker implements MinedEarningsWorker {

    /** Pool account requestor. */
    private final AccountRequestor accountRequestor;
    /** Market requestor. */
    private final MarketRequestor pairRequestor;
    /** Estimated reward requestor. */
    private final RewardRequestor rewardRequestor;

    /**
     * Creates the instance.
     *
     * @param accountRequestor pool account requestor
     * @param marketRequestor market requestor
     * @param rewardRequestor estimated reward requestor
     */
    public ETHMinedEarningsWorker(AccountRequestor accountRequestor, MarketRequestor marketRequestor, RewardRequestor rewardRequestor) {
        super();
        this.accountRequestor = accountRequestor;
        this.pairRequestor = marketRequestor;
        this.rewardRequestor = rewardRequestor;
    }

    @Override
    public MinedEarnings calculate(String walletAddress)
            throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        Account account = accountRequestor.getETHAccount(walletAddress);
        BigDecimal walletBalance = account.getWalletBalance();
        CoinMarket coinMarket = pairRequestor.getETHCoin();
        BigDecimal coinPrice = coinMarket.getPrice();
        BigDecimal balanceInUSD = walletBalance.multiply(coinPrice);
        Reward reward = rewardRequestor.getETHReward(account.getTotalHashrate());
        return new MinedEarnings(walletBalance, balanceInUSD, coinPrice, reward);
    }

}
