package com.mined.in.web.site;

import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static com.mined.in.reward.RewardType.WHAT_TO_MINE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinMarket;
import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.MarketRequestorFactory;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.reward.RewardRequestorFactory;

/**
 * Site controller.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Controller
public class SiteController {

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("page", "index");
        return "template";
    }

    @GetMapping("/{coinType}")
    public String getCoinInfo(Model model, @PathVariable CoinType coinType) throws RewardRequestorException {
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE);
        CoinInfo coinInfo = null;
        BigDecimal hashrate = new BigDecimal(0);
        switch (coinType) {
        case ETH:
            coinInfo = rewardRequestor.requestEthereumReward(hashrate).getCoinInfo();
        case ETC:
            coinInfo = rewardRequestor.requestEthereumClassicReward(hashrate).getCoinInfo();
            break;
        case ZEC:
            coinInfo = rewardRequestor.requestZcashReward(hashrate).getCoinInfo();
            break;
        default:
            break;
        }
        model.addAttribute("page", "coin_info");
        model.addAttribute("coin_info", coinInfo);
        return "template";
    }

    @ModelAttribute("coin_list")
    public List<CoinMarket> coinList() throws MarketRequestorException {
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP);
        List<CoinMarket> coinMarketList = new ArrayList<>();
        coinMarketList.add(marketRequestor.requestEthereumCoin());
        coinMarketList.add(marketRequestor.requestEthereumClassicCoin());
        coinMarketList.add(marketRequestor.requestZcashCoin());
        return coinMarketList;
    }

}
