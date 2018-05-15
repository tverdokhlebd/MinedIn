package com.tverdokhlebd.minedin.web.site;

import static com.tverdokhlebd.mining.commons.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.XMR;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;
import static java.math.RoundingMode.DOWN;

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

import com.tverdokhlebd.coin.info.CoinInfo;
import com.tverdokhlebd.coin.info.CoinInfoType;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestor;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorException;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorFactory;
import com.tverdokhlebd.coin.market.CoinMarket;
import com.tverdokhlebd.coin.market.CoinMarketType;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestor;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorException;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorFactory;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorException;
import com.tverdokhlebd.minedin.description.CoinInfoDescription;
import com.tverdokhlebd.minedin.description.CoinMarketDescription;
import com.tverdokhlebd.minedin.description.CoinRewardDescription;
import com.tverdokhlebd.minedin.description.CoinTypeDescription;
import com.tverdokhlebd.minedin.description.PoolTypeDescription;
import com.tverdokhlebd.minedin.earnings.Earnings;
import com.tverdokhlebd.minedin.earnings.worker.EarningsWorker;
import com.tverdokhlebd.minedin.earnings.worker.EarningsWorkerFactory;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

/**
 * Site controller.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Controller
public class SiteController {

    /** Text resources. */
    private final static Map<String, String> RESOURCES = new HashMap<>();
    /** Loading text resources. */
    static {
        ResourceBundle resources = ResourceBundle.getBundle(SiteController.class.getName());
        Collections.list(resources.getKeys()).forEach(key -> {
            RESOURCES.put(key, resources.getString(key));
        });
    }
    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(SiteController.class);

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
    public String getCoinInfo(Model model, @PathVariable CoinTypeDescription coinType) {
        try {
            CoinInfoRequestor coinInfoRequestor = CoinInfoRequestorFactory.create(CoinInfoType.WHAT_TO_MINE);
            CoinInfo coinInfo = coinInfoRequestor.requestCoinInfo(coinType.getCoinType());
            List<PoolTypeDescription> poolTypeList = Arrays.asList(PoolTypeDescription.values()).stream().filter(pool -> {
                return pool.getPoolType().getCoinTypeList().indexOf(coinType.getCoinType()) != -1;
            }).collect(Collectors.toList());
            model.addAttribute("coin_type", coinType);
            model.addAttribute("coin_info", coinInfo);
            model.addAttribute("pool_list", poolTypeList);
        } catch (CoinInfoRequestorException e) {
            LOG.error("Coin info request error", e);
            handleCoinInfoError(model, CoinInfoDescription.WHAT_TO_MINE, e);
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
    public String calculate(Model model, @PathVariable CoinTypeDescription coinType, @PathVariable PoolTypeDescription poolType,
            @PathVariable String walletAddress) {
        try {
            EarningsWorker worker = EarningsWorkerFactory.create(poolType,
                                                                 CoinInfoDescription.WHAT_TO_MINE,
                                                                 CoinMarketDescription.COIN_MARKET_CAP,
                                                                 CoinRewardDescription.WHAT_TO_MINE);
            Earnings earnings = worker.calculate(coinType, walletAddress);
            CoinMarket btcCoinMarket = CoinMarketRequestorFactory.create(CoinMarketType.COIN_MARKET_CAP).requestCoinMarket(BTC);
            model.addAttribute("coin_type", coinType);
            model.addAttribute("pool_info", poolType);
            model.addAttribute("coin_info", earnings.getCoinInfo());
            model.addAttribute("coin_price", earnings.getCoinMarket().getPrice());
            model.addAttribute("coin_reward", earnings.getCoinReward());
            model.addAttribute("coin_balance", earnings.getAccount().getWalletBalance());
            model.addAttribute("usd_balance", earnings.getUsdBalance());
            model.addAttribute("coin_in_btc", btcCoinMarket.getPrice().divide(earnings.getCoinMarket().getPrice(), DOWN));
        } catch (AccountRequestorException e) {
            LOG.error("Account request error", e);
            handleAccountError(model, poolType, e);
        } catch (CoinInfoRequestorException e) {
            LOG.error("Coin info request error", e);
            handleCoinInfoError(model, CoinInfoDescription.WHAT_TO_MINE, e);
        } catch (CoinMarketRequestorException e) {
            LOG.error("Coin market request error", e);
            handleCoinMarketError(model, CoinMarketDescription.COIN_MARKET_CAP, e);
        } catch (CoinRewardRequestorException e) {
            LOG.error("Coin reward request error", e);
            handleCoinRewardError(model, CoinRewardDescription.WHAT_TO_MINE, e);
        } catch (Exception e) {
            LOG.error("Calculate error", e);
            handleUnexpectedError(model, e);
        }
        model.addAttribute("page", "earnings");
        return "template";
    }

    /**
     * Requests google verification page.
     *
     * @return google verification page
     */
    @GetMapping("/google78d9213fe4e57443.html")
    public String getGoogle() {
        return "pages/google78d9213fe4e57443";
    }

    /**
     * Requests coin market list.
     *
     * @return coin market list
     */
    @ModelAttribute("coin_market_list")
    public List<CoinMarket> getCoinMarketList(Model model) {
        List<CoinMarket> coinMarketList = new ArrayList<>();
        try {
            CoinMarketRequestor coinMarketRequestor = CoinMarketRequestorFactory.create(CoinMarketType.COIN_MARKET_CAP);
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(BTC));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ETH));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(XMR));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ETC));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ZEC));
        } catch (CoinMarketRequestorException e) {
            LOG.error("Coin market request error", e);
            handleCoinMarketError(model, CoinMarketDescription.COIN_MARKET_CAP, e);
        } catch (Exception e) {
            LOG.error("Get coin market list error", e);
            handleUnexpectedError(model, e);
        }
        return coinMarketList;
    }

    /**
     * Requests coin type list.
     *
     * @return coin type list
     */
    @ModelAttribute("coin_type_list")
    public List<CoinTypeDescription> getCoinTypeList(Model model) {
        return Arrays.asList(CoinTypeDescription.values());
    }

    /**
     * Requests text resources.
     *
     * @return text resources
     */
    @ModelAttribute("resources")
    public Map<String, String> getText() {
        return RESOURCES;
    }

    /**
     * Handles unexpected error.
     *
     * @param model model attributes
     * @param exception unexpected exception
     */
    private void handleUnexpectedError(Model model, Exception exception) {
        addErrorToModel(model, RESOURCES.get("error_unexpected"), exception);
    }

    /**
     * Handles account error.
     *
     * @param model model attributes
     * @param poolType pool type
     * @param requestorException requestor exception
     */
    private void handleAccountError(Model model, PoolTypeDescription poolType, AccountRequestorException requestorException) {
        String errorMessage = String.format(RESOURCES.get("error_account"), poolType.getName());
        addErrorToModel(model, errorMessage, requestorException);
    }

    /**
     * Handles coin info error.
     *
     * @param model model attributes
     * @param coinInfo coin info
     * @param requestorException requestor exception
     */
    private void handleCoinInfoError(Model model, CoinInfoDescription coinInfo, CoinInfoRequestorException requestorException) {
        String errorMessage = String.format(RESOURCES.get("error_coin_info"), coinInfo.getName());
        addErrorToModel(model, errorMessage, requestorException);
    }

    /**
     * Handles coin market error.
     *
     * @param model model attributes
     * @param coinMarket coin market
     * @param requestorException requestor exception
     */
    private void handleCoinMarketError(Model model, CoinMarketDescription coinMarket, CoinMarketRequestorException requestorException) {
        String errorMessage = String.format(RESOURCES.get("error_coin_market"), coinMarket.getName());
        addErrorToModel(model, errorMessage, requestorException);
    }

    /**
     * Handles coin reward error.
     *
     * @param model model attributes
     * @param coinReward coin reward
     * @param requestorException requestor exception
     */
    private void handleCoinRewardError(Model model, CoinRewardDescription coinReward, CoinRewardRequestorException requestorException) {
        String errorMessage = String.format(RESOURCES.get("error_coin_reward"), coinReward.getName());
        addErrorToModel(model, errorMessage, requestorException);
    }

    /**
     * Adds error message and details to model attributes.
     *
     * @param model model attributes
     * @param errorMessage error message
     * @param exception exception
     */
    private void addErrorToModel(Model model, String errorMessage, Exception exception) {
        model.addAttribute("error_message", errorMessage);
        model.addAttribute("error_details", String.format(RESOURCES.get("error_details"), exception.getMessage()));
    }

}
