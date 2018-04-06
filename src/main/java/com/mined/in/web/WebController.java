package com.mined.in.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mined.in.coin.CoinType;
import com.mined.in.pool.PoolType;

/**
 * Controller for web API.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/web")
public class WebController {

    @RequestMapping("/coins")
    public List<CoinType> requestCoins() {
        return Arrays.asList(CoinType.values());
    }

    @RequestMapping("/pools")
    public List<PoolType> requestPools(@RequestParam("coin") String coin) {
        CoinType coinType = CoinType.valueOf(coin);
        return Arrays.asList(PoolType.values()).stream().filter(pool -> {
            return pool.getCoinTypeList().indexOf(coinType) != -1;
        }).collect(Collectors.toList());
    }

}
