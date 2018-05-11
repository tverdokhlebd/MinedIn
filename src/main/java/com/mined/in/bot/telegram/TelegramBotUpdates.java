package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.SELECT_COIN_REWARD;
import static com.mined.in.description.CoinMarketDescription.COIN_MARKET_CAP;
import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mined.in.bot.BotUpdates;
import com.mined.in.description.CoinInfoDescription;
import com.mined.in.description.CoinMarketDescription;
import com.mined.in.description.CoinRewardDescription;
import com.mined.in.description.CoinTypeDescription;
import com.mined.in.description.PoolTypeDescription;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.utils.ReadableHashrateUtil;
import com.mined.in.utils.ReadableTimeUtil;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.tverdokhlebd.coin.info.CoinInfo;
import com.tverdokhlebd.coin.info.requestor.CoinInfoRequestorException;
import com.tverdokhlebd.coin.market.requestor.CoinMarketRequestorException;
import com.tverdokhlebd.coin.reward.CoinReward;
import com.tverdokhlebd.coin.reward.requestor.CoinRewardRequestorException;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

/**
 * Handler of incoming updates from Telegram bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramBotUpdates implements BotUpdates {

    /** Pengrad telegram bot. */
    private final TelegramBot bot;
    /** Telegram incoming message. */
    private Message incomingMessage;
    /** Telegram response message. */
    private TelegramResponse responseMessage;
    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(TelegramBotUpdates.class);
    /** Text resources. */
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("bot");

    /**
     * Creates the instance.
     *
     * @param token telegram token
     */
    public TelegramBotUpdates(String token) {
        super();
        bot = new TelegramBot(token);
    }

    @Override
    public void process(String request) {
        try {
            Update update = BotUtils.parseUpdate(request);
            boolean simpleMessage = update.callbackQuery() == null;
            incomingMessage = simpleMessage ? update.message() : update.callbackQuery().message();
            String data = simpleMessage ? incomingMessage.text() : update.callbackQuery().data();
            TelegramStepData stepData = new TelegramStepData(data, simpleMessage);
            responseMessage = new TelegramResponse(stepData);
            switch (stepData.getStep()) {
            case START: {
                createStartMessage();
                break;
            }
            case ENTER_WALLET: {
                createSupportingCoinsMessage();
                break;
            }
            case SELECT_COIN_TYPE: {
                createSupportingPoolsMessage();
                break;
            }
            case SELECT_POOL_ACCOUNT: {
                // The default values, since they are the only ones
                stepData.setCoinInfo(CoinInfoDescription.WHAT_TO_MINE);
                stepData.setMarketInfo(COIN_MARKET_CAP);
                stepData.setRewardInfo(CoinRewardDescription.WHAT_TO_MINE);
                stepData.setStep(SELECT_COIN_REWARD);
            }
            case SELECT_COIN_INFO:
            case SELECT_COIN_MARKET:
            case SELECT_COIN_REWARD: {
                responseMessage.parsePreviousResultMessage(incomingMessage);
                String walletAddress = incomingMessage.replyToMessage().text();
                Earnings earnings = calculateEarnings(walletAddress);
                createMinedEarningsMessage(earnings);
                break;
            }
            }
        } catch (AccountRequestorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("pool_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (CoinMarketRequestorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("market_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (CoinRewardRequestorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("reward_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (Exception e) {
            responseMessage.setError(String.format(RESOURCE.getString("common_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } finally {
            if (responseMessage.onlySendMessage()) {
                sendMessage();
            } else {
                editMessage();
            }
        }
    }

    /**
     * Creates welcome message.
     */
    private void createStartMessage() {
        responseMessage.setMessage(RESOURCE.getString("start"));
    }

    /**
     * Creates supporting mining coin types message.
     */
    private void createSupportingCoinsMessage() {
        List<CoinTypeDescription> coinInfoList = Arrays.asList(CoinTypeDescription.values()).stream().filter(coin -> {
            return coin.isEnabled();
        }).collect(Collectors.toList());
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[coinInfoList.size()][1];
        for (int i = 0; i < coinInfoList.size(); i++) {
            CoinTypeDescription miningCoinType = coinInfoList.get(i);
            String coinSymbol = miningCoinType.name();
            keyboardButtonArray[i][0] = new InlineKeyboardButton(coinSymbol).callbackData(coinSymbol);
        }
        responseMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        responseMessage.setMessage(RESOURCE.getString("select_coin"));
    }

    /**
     * Creates supporting pool types message.
     */
    private void createSupportingPoolsMessage() {
        CoinTypeDescription coinType = responseMessage.getStepData().getCoinType();
        List<PoolTypeDescription> poolInfoList = Arrays.asList(PoolTypeDescription.values()).stream().filter(pool -> {
            return pool.getPoolType().getCoinTypeList().indexOf(coinType.getCoinType()) != -1;
        }).collect(Collectors.toList());
        poolInfoList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[poolInfoList.size()][1];
        for (int i = 0; i < poolInfoList.size(); i++) {
            PoolTypeDescription poolInfo = poolInfoList.get(i);
            String callbackData = responseMessage.getStepData().getCoinType().name() + "-" + poolInfo.name();
            keyboardButtonArray[i][0] = new InlineKeyboardButton(poolInfo.getName()).callbackData(callbackData);
        }
        responseMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        responseMessage.setMessage(RESOURCE.getString("select_pool"));
    }

    /**
     * Calculates earnings of pool account.
     *
     * @param walletAddress wallet address
     * @return earnings of pool account
     * @throws AccountRequestorException if there is any error in account requesting
     * @throws CoinInfoRequestorException if there is any error in coin info requesting
     * @throws CoinMarketRequestorException if there is any error in coin market requesting
     * @throws CoinRewardRequestorException if there is any error in coin reward requesting
     */
    private Earnings calculateEarnings(String walletAddress)
            throws AccountRequestorException, CoinMarketRequestorException, CoinRewardRequestorException, CoinInfoRequestorException {
        TelegramStepData stepData = responseMessage.getStepData();
        CoinTypeDescription coinType = stepData.getCoinType();
        CoinInfoDescription coinInfo = stepData.getCoinInfo();
        PoolTypeDescription poolInfo = stepData.getPoolInfo();
        CoinMarketDescription marketInfo = stepData.getMarketInfo();
        CoinRewardDescription rewardInfo = stepData.getRewardInfo();
        EarningsWorker minedWorker = EarningsWorkerFactory.create(poolInfo, coinInfo, marketInfo, rewardInfo);
        return minedWorker.calculate(coinType, walletAddress);
    }

    /**
     * Creates earnings of pool account message.
     *
     * @param earnings earnings of pool account
     */
    private void createMinedEarningsMessage(Earnings earnings) {
        TelegramStepData stepData = responseMessage.getStepData();
        BigDecimal coinBalance = earnings.getAccount().getWalletBalance().setScale(8, DOWN);
        BigDecimal usdBalance = earnings.getUsdBalance().setScale(2, DOWN);
        BigDecimal coinPrice = earnings.getCoinMarket().getPrice().setScale(2, DOWN);
        String balanceMessage = RESOURCE.getString("balance");
        balanceMessage = String.format(balanceMessage,
                                       stepData.getMarketInfo().getName(),
                                       "$" + usdBalance,
                                       "$" + coinPrice);
        CoinReward reward = earnings.getCoinReward();
        String accountMessage = RESOURCE.getString("account");
        accountMessage = String.format(accountMessage,
                                       stepData.getPoolInfo().getName(),
                                       coinBalance + " " + stepData.getCoinType().name(),
                                       ReadableHashrateUtil.convertToReadableHashPower(reward.getReportedHashrate()));
        BigDecimal perHour = reward.getRewardPerHour().setScale(6, DOWN);
        BigDecimal perDay = reward.getRewardPerDay().setScale(6, DOWN);
        BigDecimal perWeek = reward.getRewardPerWeek().setScale(6, DOWN);
        BigDecimal perMonth = reward.getRewardPerMonth().setScale(6, DOWN);
        BigDecimal perYear = reward.getRewardPerYear().setScale(6, DOWN);
        String rewardsMessage = RESOURCE.getString("rewards");
        rewardsMessage = String.format(rewardsMessage,
                                       stepData.getRewardInfo().getName(),
                                       perHour,
                                       "$" + perHour.multiply(coinPrice).setScale(2, DOWN),
                                       perDay,
                                       "$" + perDay.multiply(coinPrice).setScale(2, DOWN),
                                       perWeek,
                                       "$" + perWeek.multiply(coinPrice).setScale(2, DOWN),
                                       perMonth,
                                       "$" + perMonth.multiply(coinPrice).setScale(2, DOWN),
                                       perYear,
                                       "$" + perYear.multiply(coinPrice).setScale(2, DOWN));
        String infoMessage = RESOURCE.getString("info");
        CoinInfo coinInfo = earnings.getCoinInfo();
        infoMessage = String.format(infoMessage,
                                    stepData.getRewardInfo().getName(),
                                    ReadableTimeUtil.convertToReadableTime(coinInfo.getBlockTime()),
                                    coinInfo.getBlockCount(),
                                    coinInfo.getBlockReward(),
                                    ReadableHashrateUtil.convertToReadableHashPower(coinInfo.getNetworkHashrate()));
        responseMessage.setMessage(balanceMessage + accountMessage + rewardsMessage + infoMessage);
    }

    /**
     * Sends a message.
     */
    private void sendMessage() {
        SendMessage request = new SendMessage(incomingMessage.chat().id(), responseMessage.getFormattedMessage());
        request.parseMode(HTML);
        if (responseMessage.getKeyboardMarkup() != null) {
            request.replyMarkup(responseMessage.getKeyboardMarkup());
        }
        request.replyToMessageId(incomingMessage.messageId());
        BaseResponse sendResponse = bot.execute(request);
        if (!sendResponse.isOk()) {
            LOG.error(sendResponse.description());
        }
    }

    /**
     * Edits a message.
     */
    private void editMessage() {
        String finalMessage = responseMessage.getFormattedMessage();
        EditMessageText request = new EditMessageText(incomingMessage.chat().id(), incomingMessage.messageId(), finalMessage);
        request.parseMode(HTML);
        if (responseMessage.getKeyboardMarkup() != null) {
            request.replyMarkup(responseMessage.getKeyboardMarkup());
        }
        BaseResponse response = bot.execute(request);
        if (!response.isOk()) {
            LOG.error(response.description());
        }
    }

}
