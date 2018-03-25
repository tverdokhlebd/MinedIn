package com.mined.in.bot.telegram;

import static com.mined.in.calculator.CalculationType.WHAT_TO_MINE;
import static com.mined.in.market.MarketType.COIN_MARKET_CAP;
import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mined.in.bot.BotUpdates;
import com.mined.in.calculator.CalculationExecutor;
import com.mined.in.calculator.CalculationExecutorException;
import com.mined.in.calculator.CalculationExecutorFactory;
import com.mined.in.calculator.CalculationType;
import com.mined.in.coin.CoinType;
import com.mined.in.market.MarketExecutor;
import com.mined.in.market.MarketExecutorException;
import com.mined.in.market.MarketExecutorFactory;
import com.mined.in.market.MarketType;
import com.mined.in.pool.PoolType;
import com.mined.in.pool.account.AccountExecutor;
import com.mined.in.pool.account.AccountExecutorException;
import com.mined.in.pool.account.AccountExecutorFactory;
import com.mined.in.worker.MinedResult;
import com.mined.in.worker.MinedWorker;
import com.mined.in.worker.MinedWorkerFactory;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

/**
 * Class for processing incoming updates from Telegram bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramUpdatesBot implements BotUpdates {

    /** Pengrad telegram bot. */
    private final com.pengrad.telegrambot.TelegramBot bot;
    /** Telegram message. */
    private TelegramMessage telegramMessage;
    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(TelegramUpdatesBot.class);
    /** Localization resources. */
    private final static ResourceBundle LOCALIZATION = ResourceBundle.getBundle(TelegramUpdatesBot.class.getSimpleName().toLowerCase());

    /**
     * Creates the instance for processing incoming updates from Telegram bot.
     *
     * @param token telegram token
     */
    public TelegramUpdatesBot(String token) {
        super();
        bot = new com.pengrad.telegrambot.TelegramBot(token);
    }

    @Override
    public void process(String request) {
        try {
            Update update = BotUtils.parseUpdate(request);
            Message message = update.message();
            if (message != null) {
                processMessage(message);
            }
            CallbackQuery callbackQuery = update.callbackQuery();
            if (callbackQuery != null) {
                processCallbackQuery(callbackQuery);
            }
        } catch (AccountExecutorException e) {
            telegramMessage.setErrorMessage(String.format(LOCALIZATION.getString("pool_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (MarketExecutorException e) {
            telegramMessage.setErrorMessage(String.format(LOCALIZATION.getString("market_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (CalculationExecutorException e) {
            telegramMessage.setErrorMessage(String.format(LOCALIZATION.getString("mining_calculation_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (Exception e) {
            telegramMessage.setErrorMessage(LOCALIZATION.getString("unexpected_error"));
            LOG.error("Incoming updates processing error", e);
        } finally {
            if (telegramMessage.onlySendMessage()) {
                sendMessage();
            } else {
                editMessage();
            }
        }
    }

    /**
     * Processes text message.
     *
     * @param message text message
     */
    private void processMessage(Message message) {
        telegramMessage = new TelegramMessage(message.chat().id(), message.messageId());
        if (message.text().equalsIgnoreCase("/start")) {
            telegramMessage.setMessageContent(LOCALIZATION.getString("start"));
        } else {
            createSupportingCoinsMessage();
        }
    }

    /**
     * Processes callback query.
     *
     * @param callbackQuery callback query
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     * @throws CalculationExecutorException if there is any error in mining calculation creating
     */
    private void processCallbackQuery(CallbackQuery callbackQuery)
            throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        String data = callbackQuery.data();
        if (data == null || data.isEmpty()) {
            LOG.debug("CallbackQuery data is empty");
            return;
        }
        telegramMessage = new TelegramMessage(callbackQuery.message().chat().id(), callbackQuery.message().messageId());
        TelegramStepData stepData = new TelegramStepData(data);
        telegramMessage.setStepData(stepData);
        switch (stepData.getStep()) {
        case COIN: {
            createSupportingPoolsMessage(stepData);
            break;
        }
        case POOL: {
            telegramMessage.parsePreviousMessage(callbackQuery.message());
            String walletAddress = callbackQuery.message().replyToMessage().text();
            calculateAndCreateMinedResultMessage(stepData, walletAddress);
            break;
        }
        }
    }

    /**
     * Calculates and creates mined result message.
     *
     * @param stepData data of current step
     * @param walletAddress wallet address
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     * @throws CalculationExecutorException if there is any error in mining calculation creating
     */
    private void calculateAndCreateMinedResultMessage(TelegramStepData stepData, String walletAddress)
            throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        CoinType coinType = stepData.getCoin();
        PoolType poolType = stepData.getPool();
        MarketType marketType = COIN_MARKET_CAP;
        CalculationType calculationType = WHAT_TO_MINE;
        MinedResult minedResult = calculateMined(walletAddress, coinType, poolType, marketType, calculationType);
        createMinedResultMessage(coinType, poolType, minedResult);
    }

    /**
     * Calculates mined and returns mined result.
     *
     * @param walletAddress wallet address
     * @param coinType coin type
     * @param poolType pool type
     * @param marketType market type
     * @param calculationType mining calculation type
     * @return mined result
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     * @throws CalculationExecutorException if there is any error in mining calculation creating
     */
    private MinedResult calculateMined(String walletAddress, CoinType coinType, PoolType poolType, MarketType marketType,
            CalculationType calculationType)
            throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        AccountExecutor accountExecutor = AccountExecutorFactory.getAccountExecutor(poolType);
        MarketExecutor marketExecutor = MarketExecutorFactory.getMarketExecutor(marketType);
        CalculationExecutor calculationExecutor = CalculationExecutorFactory.getCalculationExecutor(calculationType);
        MinedWorker minedWorker = MinedWorkerFactory.getMinedWorker(coinType, accountExecutor, marketExecutor, calculationExecutor);
        return minedWorker.calculate(walletAddress);
    }

    /**
     * Creates mined result message.
     *
     * @param coinType coin type
     * @param poolType pool type
     * @param minedResult mined result
     */
    private void createMinedResultMessage(CoinType coinType, PoolType poolType, MinedResult minedResult) {
        BigDecimal coinBalance = minedResult.getCoinBalance().setScale(8, DOWN);
        BigDecimal usdBalance = minedResult.getUsdBalance().setScale(2, DOWN);
        BigDecimal coinPrice = minedResult.getCoinPrice().setScale(2, DOWN);
        String minedResultMessage = LOCALIZATION.getString("mined_result");
        minedResultMessage = String.format(minedResultMessage,
                                           poolType.getWebsite(),
                                           poolType.getName().toUpperCase(),
                                           coinBalance,
                                           coinType.getSymbol(),
                                           usdBalance,
                                           coinPrice);
        telegramMessage.setMessageContent(minedResultMessage);
    }

    /**
     * Creates supporting coins message.
     */
    private void createSupportingCoinsMessage() {
        List<CoinType> coinList = Arrays.asList(CoinType.values());
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[coinList.size()][1];
        for (int i = 0; i < coinList.size(); i++) {
            CoinType coin = coinList.get(i);
            String coinSymbol = coin.getSymbol();
            keyboardButtonArray[i][0] = new InlineKeyboardButton(coinSymbol).callbackData(coinSymbol);
        }
        telegramMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        telegramMessage.setMessageContent(LOCALIZATION.getString("select_coin"));
    }

    /**
     * Creates supporting pools message.
     *
     * @param stepData data of current step
     */
    private void createSupportingPoolsMessage(TelegramStepData stepData) {
        List<PoolType> poolList = Arrays.asList(PoolType.values());
        poolList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        InlineKeyboardButton[][] keyboardButtonArray = new InlineKeyboardButton[poolList.size()][1];
        for (int i = 0; i < poolList.size(); i++) {
            PoolType pool = poolList.get(i);
            String poolName = pool.getName();
            String callbackData = stepData.getCoin().getSymbol() + "_" + poolName;
            keyboardButtonArray[i][0] = new InlineKeyboardButton(poolName).callbackData(callbackData);
        }
        telegramMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        telegramMessage.setMessageContent(LOCALIZATION.getString("select_pool"));
    }

    /**
     * Sends a message.
     */
    private void sendMessage() {
        SendMessage request = new SendMessage(telegramMessage.getChatId(), telegramMessage.getFinalMessage());
        request.parseMode(HTML);
        if (telegramMessage.getKeyboardMarkup() != null) {
            request.replyMarkup(telegramMessage.getKeyboardMarkup());
        }
        if (telegramMessage.getMessageId() != null) {
            request.replyToMessageId(telegramMessage.getMessageId());
        }
        BaseResponse sendResponse = bot.execute(request);
        if (!sendResponse.isOk()) {
            LOG.error(sendResponse.description());
        }
    }

    /**
     * Edits a message.
     */
    private void editMessage() {
        String finalMessage = telegramMessage.getFinalMessage();
        EditMessageText request = new EditMessageText(telegramMessage.getChatId(), telegramMessage.getMessageId(), finalMessage);
        request.parseMode(HTML);
        if (telegramMessage.getKeyboardMarkup() != null) {
            request.replyMarkup(telegramMessage.getKeyboardMarkup());
        }
        BaseResponse response = bot.execute(request);
        if (!response.isOk()) {
            LOG.error(response.description());
        }
    }

}
