package com.mined.in.web.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
 * API controller.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * Requests supporting coin types.
     *
     * @return supporting coin types
     */
    @GetMapping("/coins")
    public ResponseEntity<ApiResponse<List<CoinType>>> requestCoins() {
        try {
            return new ApiResponse<List<CoinType>>(Arrays.asList(CoinType.values())).createSuccess();
        } catch (Exception e) {
            return new ApiResponse<List<CoinType>>().createError(e.getMessage());
        }
    }

    /**
     * Requests supporting pool types by coin type.
     *
     * @param coin coin type
     * @return supporting pool types by coin type
     */
    @GetMapping("/pools")
    public ResponseEntity<ApiResponse<List<PoolType>>> requestPools(@RequestParam("coin") String coin) {
        try {
            CoinType coinType = CoinType.valueOf(coin);
            List<PoolType> poolTypeList = Arrays.asList(PoolType.values()).stream().filter(pool -> {
                return pool.getCoinTypeList().indexOf(coinType) != -1;
            }).collect(Collectors.toList());
            return new ApiResponse<List<PoolType>>(poolTypeList).createSuccess();
        } catch (Exception e) {
            return new ApiResponse<List<PoolType>>().createError(e.getMessage());
        }
    }

    /**
     * Requests supporting market types.
     *
     * @return supporting market types
     */
    @GetMapping("/markets")
    public ResponseEntity<ApiResponse<List<MarketType>>> requestMarkets() {
        try {
            return new ApiResponse<List<MarketType>>(Arrays.asList(MarketType.values())).createSuccess();
        } catch (Exception e) {
            return new ApiResponse<List<MarketType>>().createError(e.getMessage());
        }
    }

    /**
     * Requests supporting reward types.
     *
     * @return supporting reward types
     */
    @GetMapping("/rewards")
    public ResponseEntity<ApiResponse<List<RewardType>>> requestRewards() {
        try {
            return new ApiResponse<List<RewardType>>(Arrays.asList(RewardType.values())).createSuccess();
        } catch (Exception e) {
            return new ApiResponse<List<RewardType>>().createError(e.getMessage());
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
    @GetMapping("/calculate")
    public ResponseEntity<ApiResponse<Earnings>> requestCalculation(@RequestParam("coin") String coin, @RequestParam("pool") String pool,
            @RequestParam("market") String market, @RequestParam("reward") String reward,
            @RequestParam("wallet_address") String walletAddress) {
        try {
            CoinType coinType = CoinType.valueOf(coin);
            AccountRequestor accountRequestor = AccountRequestorFactory.create(PoolType.valueOf(pool));
            MarketRequestor marketRequestor = MarketRequestorFactory.create(MarketType.valueOf(market));
            RewardRequestor rewardRequestor = RewardRequestorFactory.create(RewardType.valueOf(reward));
            EarningsWorker worker = EarningsWorkerFactory.create(coinType, accountRequestor, marketRequestor, rewardRequestor);
            Earnings earnings = worker.calculate(walletAddress);
            return new ApiResponse<Earnings>(earnings).createSuccess();
        } catch (Exception e) {
            return new ApiResponse<Earnings>().createError(e.getMessage());
        }
    }

}
