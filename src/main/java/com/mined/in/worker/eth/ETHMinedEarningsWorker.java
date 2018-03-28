package com.mined.in.worker.eth;

import java.math.BigDecimal;

import com.mined.in.market.Market;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;
import com.mined.in.pool.Account;
import com.mined.in.pool.AccountExecutor;
import com.mined.in.pool.AccountExecutorException;
import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardExecutor;
import com.mined.in.reward.RewardExecutorException;
import com.mined.in.worker.MinedEarnings;
import com.mined.in.worker.MinedEarningsWorker;

/**
 * Worker for calculating ETH mined earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHMinedEarningsWorker implements MinedEarningsWorker {

    /** Pool account executor. */
    private final AccountExecutor accountExecutor;
    /** Market executor. */
    private final MarketExecutor pairExecutor;
    /** Estimated reward executor. */
    private final RewardExecutor rewardExecutor;

    /**
     * Creates the instance.
     *
     * @param accountExecutor pool account executor
     * @param marketExecutor market executor
     * @param rewardExecutor estimated reward executor
     */
    public ETHMinedEarningsWorker(AccountExecutor accountExecutor, MarketExecutor marketExecutor, RewardExecutor rewardExecutor) {
        super();
        this.accountExecutor = accountExecutor;
        this.pairExecutor = marketExecutor;
        this.rewardExecutor = rewardExecutor;
    }

    @Override
    public MinedEarnings calculate(String walletAddress)
            throws AccountExecutorException, MarketExecutorException, RewardExecutorException {
        Account account = accountExecutor.getETHAccount(walletAddress);
        BigDecimal walletBalance = account.getWalletBalance();
        Market market = pairExecutor.getMarket();
        BigDecimal ethPrice = market.getEthPrice();
        BigDecimal balanceInUSD = walletBalance.multiply(ethPrice);
        Reward reward = rewardExecutor.getETHReward(account.getTotalHashrate());
        return new MinedEarnings(walletBalance, balanceInUSD, market.getEthPrice(), reward);
    }

}
