package com.mined.in.web.site;

import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static com.mined.in.reward.RewardType.WHAT_TO_MINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinMarket;
import com.mined.in.coin.CoinType;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.MarketRequestorFactory;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.PoolType;
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

    /** Text resources. */
    private final static ResourceBundle RESOURCES = ResourceBundle.getBundle("web");

    /**
     * Requests index page.
     *
     * @param model model attributes
     * @return index page
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("page", "index");
        return "template";
    }

    /**
     * Requests coin info page.
     *
     * @param model model attributes
     * @param coinType coin type
     * @return coin info page
     * @throws RewardRequestorException if there is any error in reward executing
     */
    @GetMapping("/{coinType}")
    public String getCoinInfo(Model model, @PathVariable CoinType coinType) throws RewardRequestorException {
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(WHAT_TO_MINE);
        CoinInfo coinInfo = null;
        switch (coinType) {
        case ETH:
            coinInfo = rewardRequestor.requestEthereumReward(null).getCoinInfo();
            break;
        case ETC:
            coinInfo = rewardRequestor.requestEthereumClassicReward(null).getCoinInfo();
            break;
        case ZEC:
            coinInfo = rewardRequestor.requestZcashReward(null).getCoinInfo();
            break;
        default:
            break;
        }
        List<PoolType> poolTypeList = Arrays.asList(PoolType.values()).stream().filter(pool -> {
            return pool.getCoinTypeList().indexOf(coinType) != -1;
        }).collect(Collectors.toList());
        model.addAttribute("page", "coin_info");
        model.addAttribute("coin_info", coinInfo);
        model.addAttribute("pool_list", poolTypeList);
        return "template";
    }

    /**
     * Requests calculation page.
     *
     * @param model model attributes
     * @param coinType coin type
     * @param poolType pool type
     * @param walletAddress wallet address
     * @return calculation page
     * @throws AccountRequestorException if there is any error in account requesting
     * @throws MarketRequestorException if there is any error in market requesting
     * @throws RewardRequestorException if there is any error in reward executing
     */
    @GetMapping("/{coinType}/{poolType}/{walletAddress}")
    public String calculate(Model model, @PathVariable CoinType coinType, @PathVariable PoolType poolType,
            @PathVariable String walletAddress) throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        EarningsWorker worker = EarningsWorkerFactory.create(coinType, poolType, COIN_MARKET_CAP, WHAT_TO_MINE);
        Earnings earnings = worker.calculate(walletAddress);
        model.addAttribute("coin_info", earnings.getEstimatedReward().getCoinInfo());
        model.addAttribute("pool_info", poolType);
        model.addAttribute("usd_balance", earnings.getUsdBalance());
        model.addAttribute("coin_balance", earnings.getCoinBalance());
        model.addAttribute("coin_price", earnings.getCoinPrice());
        model.addAttribute("reward", earnings.getEstimatedReward());
        model.addAttribute("page", "earnings_result");
        return "template";
    }

    /**
     * Requests coin list.
     *
     * @return coin list
     * @throws MarketRequestorException if there is any error in market requesting
     */
    @ModelAttribute("coin_list")
    public List<CoinMarket> getCoinList() throws MarketRequestorException {
        MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP);
        List<CoinMarket> coinMarketList = new ArrayList<>();
        coinMarketList.add(marketRequestor.requestEthereumCoin());
        coinMarketList.add(marketRequestor.requestEthereumClassicCoin());
        coinMarketList.add(marketRequestor.requestZcashCoin());
        return coinMarketList;
    }

    /**
     * Requests text resources.
     *
     * @return text resources
     */
    @ModelAttribute("text")
    public Map<String, String> getText() {
        Map<String, String> text = new HashMap<>();
        text.put("project_name", RESOURCES.getString("project_name"));
        text.put("title", RESOURCES.getString("title"));
        text.put("description", RESOURCES.getString("description"));
        text.put("navbar_brand", RESOURCES.getString("navbar_brand"));
        text.put("telegram_bot_link", RESOURCES.getString("telegram_bot_link"));
        text.put("github_link", RESOURCES.getString("github_link"));
        text.put("contact", RESOURCES.getString("contact"));
        text.put("select_coin_to_calculate", RESOURCES.getString("select_coin_to_calculate"));
        text.put("select_pool", RESOURCES.getString("select_pool"));
        text.put("calculate", RESOURCES.getString("calculate"));
        text.put("enter_wallet_address", RESOURCES.getString("enter_wallet_address"));
        text.put("coin_info", RESOURCES.getString("coin_info"));
        text.put("coin_name", RESOURCES.getString("coin_name"));
        text.put("coin_symbol", RESOURCES.getString("coin_symbol"));
        text.put("coin_website", RESOURCES.getString("coin_website"));
        text.put("coin_block_time", RESOURCES.getString("coin_block_time"));
        text.put("coin_block_reward", RESOURCES.getString("coin_block_reward"));
        text.put("coin_block_count", RESOURCES.getString("coin_block_count"));
        text.put("coin_difficulty", RESOURCES.getString("coin_difficulty"));
        text.put("coin_network_hashrate", RESOURCES.getString("coin_network_hashrate"));
        text.put("per", RESOURCES.getString("per"));
        text.put("usd", RESOURCES.getString("usd"));
        text.put("hour", RESOURCES.getString("hour"));
        text.put("day", RESOURCES.getString("day"));
        text.put("week", RESOURCES.getString("week"));
        text.put("month", RESOURCES.getString("month"));
        text.put("year", RESOURCES.getString("year"));
        text.put("your_balance_is", RESOURCES.getString("your_balance_is"));
        text.put("balance", RESOURCES.getString("balance"));
        text.put("hashrate", RESOURCES.getString("hashrate"));
        text.put("estimated_rewards", RESOURCES.getString("estimated_rewards"));
        return text;
    }

}
