package com.mined.in.worker.eth;

import java.math.BigDecimal;

import com.mined.in.exchanger.currencypair.CurrencyPair;
import com.mined.in.exchanger.currencypair.CurrencyPairExecutor;
import com.mined.in.pool.account.Account;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.worker.MinedResult;
import com.mined.in.worker.MinedWorker;
import com.mined.in.worker.MinedWorkerException;

/**
 * Class for calculating of mined.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHMinedWorker implements MinedWorker {

    /** Pool account executor. */
    private final AccountExecutor accountExecutor;
    /** Exchanger currency pair executor. */
    private final CurrencyPairExecutor currencyPairExecutor;

    /**
     * Creates the mined calculation instance.
     *
     * @param accountExecutor pool account executor
     * @param currencyPairExecutor exchanger currency pair executor
     */
    public ETHMinedWorker(AccountExecutor accountExecutor, CurrencyPairExecutor currencyPairExecutor) {
        super();
        this.accountExecutor = accountExecutor;
        this.currencyPairExecutor = currencyPairExecutor;
    }

    @Override
    public MinedResult calculate(String walletAddress) throws MinedWorkerException {
        try {
            Account account = accountExecutor.getETHAccount(walletAddress);
            BigDecimal walletBalance = account.getWalletBalance();
            CurrencyPair currencyPair = currencyPairExecutor.getETHUSDPair();
            BigDecimal usdBuyRate = currencyPair.getBuyPrice();
            BigDecimal balanceInUSD = walletBalance.multiply(usdBuyRate);
            return new MinedResult(walletBalance, balanceInUSD, currencyPair.getBuyPrice(), currencyPair.getSellPrice());
        } catch (Exception e) {
            throw new MinedWorkerException();
        }
    }

}
