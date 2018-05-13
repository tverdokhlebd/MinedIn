package com.mined.in.web.site;

import static com.tverdokhlebd.mining.commons.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.XMR;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;

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

import com.mined.in.description.CoinInfoDescription;
import com.mined.in.description.CoinMarketDescription;
import com.mined.in.description.CoinRewardDescription;
import com.mined.in.description.CoinTypeDescription;
import com.mined.in.description.PoolTypeDescription;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
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
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

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
     * Requests google verification page.
     *
     * @return google verification page
     */
    @GetMapping("/google78d9213fe4e57443.html")
    public String getGoogle() {
        return "pages/google78d9213fe4e57443";
    }

    /**
     * Requests contact page.
     *
     * @param model model attributes
     * @return contact page
     */
    @GetMapping("/contact")
    public String getContact(Model model) {
        model.addAttribute("page", "contact");
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
            LOG.error("Reward request error", e);
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
            model.addAttribute("coin_type", coinType);
            model.addAttribute("coin_info", earnings.getCoinInfo());
            model.addAttribute("pool_info", poolType);
            model.addAttribute("usd_balance", earnings.getUsdBalance());
            model.addAttribute("coin_balance", earnings.getAccount().getWalletBalance());
            model.addAttribute("coin_price", earnings.getCoinMarket().getPrice());
            model.addAttribute("reward", earnings.getCoinReward());
        } catch (AccountRequestorException e) {
            LOG.error("Account request error", e);
            handleAccountError(model, poolType, e);
        } catch (CoinInfoRequestorException e) {
            LOG.error("Info request error", e);
            handleCoinInfoError(model, CoinInfoDescription.WHAT_TO_MINE, e);
        } catch (CoinMarketRequestorException e) {
            LOG.error("Market request error", e);
            handleCoinMarketError(model, CoinMarketDescription.COIN_MARKET_CAP, e);
        } catch (CoinRewardRequestorException e) {
            LOG.error("Reward request error", e);
            handleCoinRewardError(model, CoinRewardDescription.WHAT_TO_MINE, e);
        } catch (Exception e) {
            LOG.error("Calculate error", e);
            handleUnexpectedError(model, e);
        }
        model.addAttribute("page", "earnings");
        return "template";
    }

    /**
     * Requests market coin list.
     *
     * @return market coin list
     */
    @ModelAttribute("coin_market_list")
    public List<CoinMarket> getCoinMarketList(Model model) {
        try {
            CoinMarketRequestor coinMarketRequestor = CoinMarketRequestorFactory.create(CoinMarketType.COIN_MARKET_CAP);
            List<CoinMarket> coinMarketList = new ArrayList<>();
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(BTC));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ETH));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(XMR));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ETC));
            coinMarketList.add(coinMarketRequestor.requestCoinMarket(ZEC));
            return coinMarketList;
        } catch (CoinMarketRequestorException e) {
            LOG.error("Market request error", e);
            handleCoinMarketError(model, CoinMarketDescription.COIN_MARKET_CAP, e);
            return new ArrayList<>();
        } catch (Exception e) {
            LOG.error("Get coin list error", e);
            handleUnexpectedError(model, e);
            return new ArrayList<>();
        }
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
     * Handles account error.
     *
     * @param model model attributes
     * @param poolType pool type
     * @param requestorException account error
     */
    private void handleAccountError(Model model, PoolTypeDescription poolType, AccountRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_account"), poolType.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Handles coin info error.
     *
     * @param model model attributes
     * @param coinInfoDescription coin info description
     * @param requestorException coin info error
     */
    private void handleCoinInfoError(Model model, CoinInfoDescription coinInfoDescription, CoinInfoRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_info"), coinInfoDescription.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Handles coin market error.
     *
     * @param model model attributes
     * @param coinMarketDescription coin market description
     * @param requestorException coin market error
     */
    private void handleCoinMarketError(Model model, CoinMarketDescription coinMarketDescription,
            CoinMarketRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_market"), coinMarketDescription.getName());
        String errorDetails = String.format(TEXT.get("error_details"), requestorException.getMessage());
        addErrorToModel(model, errorMessage, errorDetails);
    }

    /**
     * Handles coin reward error.
     *
     * @param model model attributes
     * @param coinRewardDescription coin reward description
     * @param requestorException coin reward error
     */
    private void handleCoinRewardError(Model model, CoinRewardDescription coinRewardDescription,
            CoinRewardRequestorException requestorException) {
        String errorMessage = String.format(TEXT.get("error_reward"), coinRewardDescription.getName());
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
