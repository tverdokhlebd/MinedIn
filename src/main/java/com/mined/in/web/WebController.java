package com.mined.in.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mined.in.coin.CoinType;

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

}
