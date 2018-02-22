package com.mining.profit.worker.eth;

import java.math.BigDecimal;

import com.mining.profit.exchanger.currencypair.CurrencyPair;
import com.mining.profit.exchanger.currencypair.CurrencyPairExecutor;
import com.mining.profit.pool.account.Account;
import com.mining.profit.pool.account.AccountExecutor;
import com.mining.profit.worker.MinedResult;
import com.mining.profit.worker.MinedWorker;
import com.mining.profit.worker.MinedWorkerException;

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
            return new MinedResult(walletBalance, balanceInUSD, usdBuyRate);
        } catch (Exception e) {
            throw new MinedWorkerException();
        }
    }

}
