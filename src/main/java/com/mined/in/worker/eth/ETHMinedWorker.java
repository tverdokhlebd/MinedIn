package com.mined.in.worker.eth;

import java.math.BigDecimal;

import com.mined.in.exchanger.pair.Pair;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
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
    /** Exchanger pair executor. */
    private final PairExecutor pairExecutor;

    /**
     * Creates the mined calculation instance.
     *
     * @param accountExecutor pool account executor
     * @param pairExecutor exchanger pair executor
     */
    public ETHMinedWorker(AccountExecutor accountExecutor, PairExecutor pairExecutor) {
        super();
        this.accountExecutor = accountExecutor;
        this.pairExecutor = pairExecutor;
    }

    @Override
    public MinedResult calculate(String walletAddress) throws AccountExecutorException, PairExecutorException {
        Account account = accountExecutor.getETHAccount(walletAddress);
        BigDecimal walletBalance = account.getWalletBalance();
        Pair pair = pairExecutor.getETHUSDPair();
        BigDecimal usdBuyRate = pair.getBuyPrice();
        BigDecimal balanceInUSD = walletBalance.multiply(usdBuyRate);
        return new MinedResult(walletBalance, balanceInUSD, pair.getBuyPrice(), pair.getSellPrice());
    }

}
