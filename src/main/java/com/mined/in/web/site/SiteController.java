package com.mined.in.web.site;

import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static com.mined.in.reward.RewardType.WHAT_TO_MINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mined.in.market.MarketType;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.reward.RewardRequestorFactory;
import com.mined.in.reward.RewardType;

/**
 * Site controller.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Controller
public class SiteController {

    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(SiteController.class);
    /** Text resources. */
    private final static Map<String, String> TEXT = new HashMap<>();
    /** Loading text resources. */
    static {
        ResourceBundle resources = ResourceBundle.getBundle("web");
        Collections.list(resources.getKeys()).forEach(key -> {
            TEXT.put(key, resources.getString(key));
        });
    }

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
     */
    @GetMapping("/{coinType}")
    public String getCoinInfo(Model model, @PathVariable CoinType coinType) throws RewardRequestorException {
        try {
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
            model.addAttribute("coin_info", coinInfo);
            model.addAttribute("pool_list", poolTypeList);
        } catch (RewardRequestorException e) {
            LOG.error("Reward request error", e);
            handleRewardError(model, WHAT_TO_MINE, e);
        } catch (Exception e) {
            LOG.error("Get coin info error", e);
            handleUnexpectedError(model, e);
        }
        model.addAttribute("page", "coin");
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
     */
    @GetMapping("/{coinType}/{poolType}/{walletAddress}")
    public String calculate(Model model, @PathVariable CoinType coinType, @PathVariable PoolType poolType,
            @PathVariable String walletAddress) throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        try {
            EarningsWorker worker = EarningsWorkerFactory.create(coinType, poolType, COIN_MARKET_CAP, WHAT_TO_MINE);
            Earnings earnings = worker.calculate(walletAddress);
            model.addAttribute("coin_info", earnings.getEstimatedReward().getCoinInfo());
            model.addAttribute("pool_info", poolType);
            model.addAttribute("usd_balance", earnings.getUsdBalance());
            model.addAttribute("coin_balance", earnings.getCoinBalance());
            model.addAttribute("coin_price", earnings.getCoinPrice());
            model.addAttribute("reward", earnings.getEstimatedReward());
        } catch (AccountRequestorException e) {
            LOG.error("Account request error", e);
            handleAccountError(model, poolType, e);
        } catch (MarketRequestorException e) {
            LOG.error("Market request error", e);
            handleMarketError(model, COIN_MARKET_CAP, e);
        } catch (RewardRequestorException e) {
            LOG.error("Reward request error", e);
            handleRewardError(model, WHAT_TO_MINE, e);
        } catch (Exception e) {
            LOG.error("Calculate error", e);
            handleUnexpectedError(model, e);
        }
        model.addAttribute("page", "earnings");
        return "template";
    }

    /**
     * Requests coin list.
     *
     * @return coin list
     */
    @ModelAttribute("coin_list")
    public List<CoinMarket> getCoinList(Model model) {
        try {
            MarketRequestor marketRequestor = MarketRequestorFactory.create(COIN_MARKET_CAP);
            List<CoinMarket> coinMarketList = new ArrayList<>();
            coinMarketList.add(marketRequestor.requestEthereumCoin());
            coinMarketList.add(marketRequestor.requestEthereumClassicCoin());
            coinMarketList.add(marketRequestor.requestZcashCoin());
            return coinMarketList;
        } catch (MarketRequestorException e) {
            LOG.error("Market request error", e);
            handleMarketError(model, COIN_MARKET_CAP, e);
            return new ArrayList<>();
        } catch (Exception e) {
            LOG.error("Get coin list error", e);
            handleUnexpectedError(model, e);
            return new ArrayList<>();
        }
    }

    /**
     * Requests text resources.
     *
     * @return text resources
     */
    @ModelAttribute("text")
    public Map<String, String> getText() {
        return TEXT;
    }

    /**
     * Handles unexpected error.
     *
     * @param model model attributes
     * @param exception unexpected error
     */
    private void handleUnexpectedError(Model model, Exception exception) {
        addErrorToModel(model, TEXT.get("error_unexpected"), exception.getMessage());
    }

    /**
     * Handles pool account error.
     *
     * @param model model attributes
     * @param poolType pool type
     * @param requestorException account error
     */
    private void handleAccountError(Model model, PoolType poolType, AccountRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_account"), poolType.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Handles market error.
     *
     * @param model model attributes
     * @param marketType market type
     * @param requestorException market error
     */
    private void handleMarketError(Model model, MarketType marketType, MarketRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_market"), marketType.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Handles reward error.
     *
     * @param model model attributes
     * @param rewardType reward type
     * @param requestorException reward error
     */
    private void handleRewardError(Model model, RewardType rewardType, RewardRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_reward"), rewardType.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Adds error message and details to model attributes.
     *
     * @param model model attributes
     * @param errorMessage error message
     * @param errorDetails error details
     */
    private void addErrorToModel(Model model, String errorMessage, String errorDetails) {
        model.addAttribute("error_message", errorMessage);
        model.addAttribute("error_details", errorDetails);
    }

}
