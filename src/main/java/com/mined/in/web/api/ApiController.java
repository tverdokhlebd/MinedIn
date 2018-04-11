package com.mined.in.web.api;

import static com.mined.in.web.api.ApiResponse.ErrorType.COMMON;
import static com.mined.in.web.api.ApiResponse.ErrorType.MARKET;
import static com.mined.in.web.api.ApiResponse.ErrorType.POOL;
import static com.mined.in.web.api.ApiResponse.ErrorType.REWARD;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.mined.in.coin.CoinType;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.MarketType;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.RewardRequestorException;
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
        return new ApiResponse<List<CoinType>>(Arrays.asList(CoinType.values())).create();
    }

    /**
     * Requests supporting pool types by coin type.
     *
     * @param coinType coin type
     * @return supporting pool types by coin type
     */
    @GetMapping("/pools")
    public ResponseEntity<ApiResponse<List<PoolType>>> requestPools(@RequestParam("coin") CoinType coinType) {
        List<PoolType> poolTypeList = Arrays.asList(PoolType.values()).stream().filter(pool -> {
            return pool.getCoinTypeList().indexOf(coinType) != -1;
        }).collect(Collectors.toList());
        return new ApiResponse<List<PoolType>>(poolTypeList).create();
    }

    /**
     * Requests supporting market types.
     *
     * @return supporting market types
     */
    @GetMapping("/markets")
    public ResponseEntity<ApiResponse<List<MarketType>>> requestMarkets() {
        return new ApiResponse<List<MarketType>>(Arrays.asList(MarketType.values())).create();
    }

    /**
     * Requests supporting reward types.
     *
     * @return supporting reward types
     */
    @GetMapping("/rewards")
    public ResponseEntity<ApiResponse<List<RewardType>>> requestRewards() {
        return new ApiResponse<List<RewardType>>(Arrays.asList(RewardType.values())).create();
    }

    /**
     * Calculates earnings of pool account.
     *
     * @param coinType coin type
     * @param poolType pool type
     * @param marketType market type
     * @param rewardType reward type
     * @param walletAddress wallet address
     * @return earnings of pool account
     */
    @GetMapping("/calculate")
    public ResponseEntity<ApiResponse<Earnings>> requestCalculation(
            @RequestParam("coin") CoinType coinType,
            @RequestParam("pool") PoolType poolType,
            @RequestParam("market") MarketType marketType,
            @RequestParam("reward") RewardType rewardType,
            @RequestParam("wallet_address") String walletAddress) {
        try {
            EarningsWorker worker = EarningsWorkerFactory.create(coinType, poolType, marketType, rewardType);
            Earnings earnings = worker.calculate(walletAddress);
            return new ApiResponse<Earnings>(earnings).create();
        } catch (AccountRequestorException e) {
            return new ApiResponse<Earnings>().create(POOL, e.getMessage());
        } catch (MarketRequestorException e) {
            return new ApiResponse<Earnings>().create(MARKET, e.getMessage());
        } catch (RewardRequestorException e) {
            return new ApiResponse<Earnings>().create(REWARD, e.getMessage());
        }
    }

    /**
     * Handles all exceptions.
     *
     * @param exception exception
     * @param request web request
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception exception, WebRequest request) {
        return new ApiResponse<String>().create(COMMON, exception.getMessage());
    }

}
