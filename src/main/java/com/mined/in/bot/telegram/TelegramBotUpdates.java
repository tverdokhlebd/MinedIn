package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.REWARD;
import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static com.mined.in.reward.RewardType.WHAT_TO_MINE;
import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mined.in.bot.BotUpdates;
import com.mined.in.coin.CoinInfo;
import com.mined.in.coin.CoinType;
import com.mined.in.earnings.Earnings;
import com.mined.in.earnings.worker.EarningsWorker;
import com.mined.in.earnings.worker.EarningsWorkerFactory;
import com.mined.in.market.MarketRequestor;
import com.mined.in.market.MarketRequestorException;
import com.mined.in.market.MarketRequestorFactory;
import com.mined.in.market.MarketType;
import com.mined.in.pool.AccountRequestor;
import com.mined.in.pool.AccountRequestorException;
import com.mined.in.pool.AccountRequestorFactory;
import com.mined.in.pool.PoolType;
import com.mined.in.reward.Reward;
import com.mined.in.reward.RewardRequestor;
import com.mined.in.reward.RewardRequestorException;
import com.mined.in.reward.RewardRequestorFactory;
import com.mined.in.reward.RewardType;
import com.mined.in.utils.HashrateConverter;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

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
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("text");

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
            case WALLET: {
                createSupportingCoinsMessage();
                break;
            }
            case COIN: {
                createSupportingPoolsMessage();
                break;
            }
            case POOL: {
                // The default values, since they are the only ones
                stepData.setMarketType(COIN_MARKET_CAP);
                stepData.setRewardType(WHAT_TO_MINE);
                stepData.setStep(REWARD);
            }
            case MARKET:
            case REWARD: {
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
        } catch (MarketRequestorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("market_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (RewardRequestorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("reward_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (Exception e) {
            responseMessage.setError(RESOURCE.getString("unexpected_error"));
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
     * Creates supporting coin types message.
     */
    private void createSupportingCoinsMessage() {
        List<CoinType> coinList = Arrays.asList(CoinType.values());
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[coinList.size()][1];
        for (int i = 0; i < coinList.size(); i++) {
            CoinType coin = coinList.get(i);
            String coinSymbol = coin.getSymbol();
            keyboardButtonArray[i][0] = new InlineKeyboardButton(coinSymbol).callbackData(coinSymbol);
        }
        responseMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        responseMessage.setMessage(RESOURCE.getString("select_coin"));
    }

    /**
     * Creates supporting pool types message.
     */
    private void createSupportingPoolsMessage() {
        List<PoolType> poolList = Arrays.asList(PoolType.values());
        poolList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[poolList.size()][1];
        for (int i = 0; i < poolList.size(); i++) {
            PoolType pool = poolList.get(i);
            String poolName = pool.getName();
            String callbackData = responseMessage.getStepData().getCoinType().getSymbol() + "_" + poolName;
            keyboardButtonArray[i][0] = new InlineKeyboardButton(poolName).callbackData(callbackData);
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
     * @throws MarketRequestorException if there is any error in market requesting
     * @throws RewardRequestorException if there is any error in estimated reward requesting
     */
    private Earnings calculateEarnings(String walletAddress)
            throws AccountRequestorException, MarketRequestorException, RewardRequestorException {
        TelegramStepData stepData = responseMessage.getStepData();
        CoinType coinType = stepData.getCoinType();
        PoolType poolType = stepData.getPoolType();
        MarketType marketType = stepData.getMarketType();
        RewardType rewardType = stepData.getRewardType();
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType);
        MarketRequestor marketRequestor = MarketRequestorFactory.create(marketType);
        RewardRequestor rewardRequestor = RewardRequestorFactory.create(rewardType);
        EarningsWorker minedWorker = EarningsWorkerFactory.create(coinType, accountRequestor, marketRequestor, rewardRequestor);
        return minedWorker.calculate(walletAddress);
    }

    /**
     * Creates earnings of pool account message.
     *
     * @param earnings earnings of pool account
     */
    private void createMinedEarningsMessage(Earnings earnings) {
        TelegramStepData stepData = responseMessage.getStepData();
        BigDecimal coinBalance = earnings.getCoinBalance().setScale(8, DOWN);
        BigDecimal usdBalance = earnings.getUsdBalance().setScale(2, DOWN);
        BigDecimal coinPrice = earnings.getCoinPrice().setScale(2, DOWN);
        String balanceMessage = RESOURCE.getString("balance");
        balanceMessage = String.format(balanceMessage,
                                       "$" + usdBalance,
                                       "$" + coinPrice);
        Reward reward = earnings.getEstimatedReward();
        String accountMessage = RESOURCE.getString("account");
        accountMessage = String.format(accountMessage,
                                       stepData.getPoolType().getName(),
                                       coinBalance + " " + stepData.getCoinType().getSymbol(),
                                       stepData.getPoolType().getName(),
                                       reward.getTotalHashrate().setScale(2, DOWN) + " " + RESOURCE.getString("mh_s"));
        BigDecimal perHour = reward.getRewardPerHour().setScale(6, DOWN);
        BigDecimal perDay = reward.getRewardPerDay().setScale(6, DOWN);
        BigDecimal perWeek = reward.getRewardPerWeek().setScale(6, DOWN);
        BigDecimal perMonth = reward.getRewardPerMonth().setScale(6, DOWN);
        BigDecimal perYear = reward.getRewardPerYear().setScale(6, DOWN);
        String rewardsMessage = RESOURCE.getString("rewards");
        CoinInfo coinInfo = reward.getCoinInfo();
        rewardsMessage = String.format(rewardsMessage,
                                       perHour,
                                       "$" + perHour.multiply(coinPrice).setScale(2, DOWN),
                                       perDay,
                                       "$" + perDay.multiply(coinPrice).setScale(2, DOWN),
                                       perWeek,
                                       "$" + perWeek.multiply(coinPrice).setScale(2, DOWN),
                                       perMonth,
                                       "$" + perMonth.multiply(coinPrice).setScale(2, DOWN),
                                       perYear,
                                       "$" + perYear.multiply(coinPrice).setScale(2, DOWN),
                                       coinInfo.getBlockTime().toPlainString() + "s",
                                       coinInfo.getBlockCount(),
                                       coinInfo.getDifficulty(),
                                       HashrateConverter.convertToReadableHashPower(coinInfo.getNetworkHashrate()));
        responseMessage.setMessage(balanceMessage + accountMessage + rewardsMessage);
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
