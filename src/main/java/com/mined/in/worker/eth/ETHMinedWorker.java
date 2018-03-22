package com.mined.in.worker.eth;

import java.math.BigDecimal;

import com.mined.in.market.Market;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;
import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.worker.MinedResult;
import com.mined.in.worker.MinedWorker;

/**
 * Worker for calculating of ETH mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHMinedWorker implements MinedWorker {

    /** Pool account executor. */
    private final AccountExecutor accountExecutor;
    /** Market executor. */
    private final MarketExecutor pairExecutor;

    /**
     * Creates the mined calculation instance.
     *
     * @param accountExecutor pool account executor
     * @param marketExecutor market executor
     */
    public ETHMinedWorker(AccountExecutor accountExecutor, MarketExecutor marketExecutor) {
        super();
        this.accountExecutor = accountExecutor;
        this.pairExecutor = marketExecutor;
    }

    @Override
    public MinedResult calculate(String walletAddress) throws AccountExecutorException, MarketExecutorException {
        Account account = accountExecutor.getETHAccount(walletAddress);
        BigDecimal walletBalance = account.getWalletBalance();
        Market market = pairExecutor.getMarket();
        BigDecimal ethPrice = market.getEthPrice();
        BigDecimal balanceInUSD = walletBalance.multiply(ethPrice);
        return new MinedResult(walletBalance, balanceInUSD, market.getEthPrice());
    }

}
