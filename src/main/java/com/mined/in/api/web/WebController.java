package com.mined.in.api.web;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mined.in.coin.CoinType;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorFactory;
import com.mined.in.market.MarketType;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorFactory;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorFactory;
import com.mined.in.reward.RewardType;

/**
 * Controller for web API.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/web")
public class WebController {

    /**
     * Requests supporting coin types.
     *
     * @return supporting coin types
     */
    @RequestMapping("/coins")
    public ResponseEntity<WebResponse<List<CoinType>>> requestCoins() {
        try {
            return new WebResponse<List<CoinType>>(Arrays.asList(CoinType.values())).create();
        } catch (Exception e) {
            return new WebResponse<List<CoinType>>().create(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Requests supporting pool types by coin type.
     *
     * @param coin coin type
     * @return supporting pool types by coin type
     */
    @RequestMapping("/pools")
    public ResponseEntity<WebResponse<List<PoolType>>> requestPools(@RequestParam("coin") String coin) {
        try {
            CoinType coinType = CoinType.valueOf(coin);
            List<PoolType> poolTypeList = Arrays.asList(PoolType.values()).stream().filter(pool -> {
                return pool.getCoinTypeList().indexOf(coinType) != -1;
            }).collect(Collectors.toList());
            return new WebResponse<List<PoolType>>(poolTypeList).create();
        } catch (Exception e) {
            return new WebResponse<List<PoolType>>().create(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Requests supporting market types.
     *
     * @return supporting market types
     */
    @RequestMapping("/markets")
    public ResponseEntity<WebResponse<List<MarketType>>> requestMarkets() {
        try {
            return new WebResponse<List<MarketType>>(Arrays.asList(MarketType.values())).create();
        } catch (Exception e) {
            return new WebResponse<List<MarketType>>().create(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Requests supporting reward types.
     *
     * @return supporting reward types
     */
    @RequestMapping("/rewards")
    public ResponseEntity<WebResponse<List<RewardType>>> requestRewards() {
        try {
            return new WebResponse<List<RewardType>>(Arrays.asList(RewardType.values())).create();
        } catch (Exception e) {
            return new WebResponse<List<RewardType>>().create(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Calculates earnings of pool account.
     *
     * @param coin coin type
     * @param pool pool type
     * @param market market type
     * @param reward reward type
     * @param walletAddress wallet address
     * @return earnings of pool account
     */
    @RequestMapping("/calculate")
    public ResponseEntity<WebResponse<Earnings>> requestCalculation(@RequestParam("coin") String coin, @RequestParam("pool") String pool,
            @RequestParam("market") String market, @RequestParam("reward") String reward,
            @RequestParam("wallet_address") String walletAddress) {
        try {
            CoinType coinType = CoinType.valueOf(coin);
            AccountRequestor accountRequestor = AccountRequestorFactory.create(PoolType.valueOf(pool));
            MarketRequestor marketRequestor = MarketRequestorFactory.create(MarketType.valueOf(market));
            RewardRequestor rewardRequestor = RewardRequestorFactory.create(RewardType.valueOf(reward));
            EarningsWorker worker = EarningsWorkerFactory.create(coinType, accountRequestor, marketRequestor, rewardRequestor);
            Earnings earnings = worker.calculate(walletAddress);
            return new WebResponse<Earnings>(earnings).create();
        } catch (Exception e) {
            return new WebResponse<Earnings>().create(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
