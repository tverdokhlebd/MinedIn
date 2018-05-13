package com.tverdokhlebd.minedin.earnings.worker;

import java.math.BigDecimal;

import com.tverdokhlebd.coin.info.CoinInfo;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestor;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorException;
import com.tverdokhlebd.coin.market.CoinMarket;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestor;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorException;
import com.tverdokhlebd.coin.reward.CoinReward;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestor;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorException;
import com.tverdokhlebd.minedin.description.CoinTypeDescription;
import com.tverdokhlebd.minedin.earnings.Earnings;
import com.tverdokhlebd.mining.commons.coin.CoinType;
import com.tverdokhlebd.mining.pool.Account;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestor;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

/**
 * Worker for calculating earnings.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class MinedInWorker implements EarningsWorker {

    /** Account requestor. */
    private final AccountRequestor accountRequestor;
    /** Coin info requestor. */
    private final CoinInfoRequestor coinInfoRequestor;
    /** Coin market requestor. */
    private final CoinMarketRequestor coinMarketRequestor;
    /** Coin reward requestor. */
    private final CoinRewardRequestor coinRewardRequestor;

    /**
     * Creates instance.
     *
     * @param accountRequestor account requestor
     * @param coinInfoRequestor coin info requestor
     * @param coinMarketRequestor coin market requestor
     * @param coinRewardRequestor coin reward requestor
     */
    public MinedInWorker(AccountRequestor accountRequestor, CoinInfoRequestor coinInfoRequestor, CoinMarketRequestor coinMarketRequestor,
            CoinRewardRequestor coinRewardRequestor) {
        super();
        this.accountRequestor = accountRequestor;
        this.coinInfoRequestor = coinInfoRequestor;
        this.coinMarketRequestor = coinMarketRequestor;
        this.coinRewardRequestor = coinRewardRequestor;
    }

    @Override
    public Earnings calculate(CoinTypeDescription coin, String walletAddress)
            throws AccountRequestorException, CoinInfoRequestorException, CoinMarketRequestorException, CoinRewardRequestorException {
        CoinType coinType = coin.getCoinType();
        Account account = accountRequestor.requestAccount(coinType, walletAddress);
        CoinInfo coinInfo = coinInfoRequestor.requestCoinInfo(coinType);
        CoinMarket coinMarket = coinMarketRequestor.requestCoinMarket(coinType);
        CoinReward coinReward = coinRewardRequestor.requestCoinReward(coinType, account.getReportedHashrate());
        BigDecimal usdBalance = account.getWalletBalance().multiply(coinMarket.getPrice());
        return new Earnings(usdBalance, account, coinInfo, coinMarket, coinReward);
    }

}
