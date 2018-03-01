package com.mined.in.bot.telegram;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mined.in.bot.BotUpdates;
import com.mined.in.coin.Coin;
import com.mined.in.exchanger.Exchanger;
import com.mined.in.exchanger.pair.PairExecutor;
import com.mined.in.exchanger.pair.PairExecutorException;
import com.mined.in.exchanger.pair.PairExecutorFactory;
import com.mined.in.pool.Pool;
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
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Class for processing incoming updates from Telegram bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramUpdatesBot implements BotUpdates {

    /** Telegram bot. */
    private final com.pengrad.telegrambot.TelegramBot bot;
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
        } catch (Exception e) {
            LOG.error("Error processing of incoming updates", e);
        }
    }

    /**
     * Processes text message.
     *
     * @param message text message
     */
    private void processMessage(Message message) {
        Long chatId = message.chat().id();
        if (message.text().equalsIgnoreCase("/start")) {

        } else {
            sendSupportingCoins(chatId, message.messageId());
        }
    }

    /**
     * Processes callback query.
     *
     * @param callbackQuery callback query
     * @throws AccountExecutorException if there is any error in account creating
     * @throws PairExecutorException if there is any error in pair creating
     */
    private void processCallbackQuery(CallbackQuery callbackQuery) throws AccountExecutorException, PairExecutorException {
        String data = callbackQuery.data();
        if (data == null || data.isEmpty()) {
            return;
        }
        Object chatId = callbackQuery.message().chat().id();
        Integer messageId = callbackQuery.message().messageId();
        TelegramStepData stepData = new TelegramStepData(data);
        switch (stepData.getStep()) {
        case COIN: {
            sendSupportingPools(chatId, messageId, stepData);
            break;
        }
        case POOL: {
            sendSupportingExchangers(chatId, messageId, stepData);
            break;
        }
        case EXCHANGER: {
            String walletAddress = callbackQuery.message().replyToMessage().text();
            calculateAndSendMinedResult(chatId, messageId, stepData, walletAddress);
            break;
        }
        }
    }

    /**
     * Calculates and sends mined result.
     *
     * @param chatId unique identifier for chat
     * @param messageId unique identifier for message
     * @param stepData data of current step
     * @param walletAddress wallet address
     * @throws AccountExecutorException if there is any error in account creating
     * @throws PairExecutorException if there is any error in pair creating
     */
    private void calculateAndSendMinedResult(Object chatId, Integer messageId, TelegramStepData stepData, String walletAddress)
            throws AccountExecutorException, PairExecutorException {
        Coin coin = stepData.getCoin();
        Pool pool = stepData.getPool();
        Exchanger exchanger = stepData.getExchanger();
        MinedResult minedResult = calculateMined(walletAddress, coin, pool, exchanger);
        sendMinedResult(chatId, messageId, coin, pool, exchanger, minedResult);
    }

    /**
     * Calculates mined and returns mined result.
     *
     * @param walletAddress wallet address
     * @param coin coin type
     * @param pool pool type
     * @param exchanger exchange type
     * @return mined result
     * @throws AccountExecutorException if there is any error in account creating
     * @throws PairExecutorException if there is any error in pair creating
     */
    private MinedResult calculateMined(String walletAddress, Coin coin, Pool pool, Exchanger exchanger)
            throws AccountExecutorException, PairExecutorException {
        AccountExecutor accountExecutor = AccountExecutorFactory.getAccountExecutor(pool);
        PairExecutor pairExecutor = PairExecutorFactory.getPairExecutor(exchanger);
        MinedWorker minedWorker = MinedWorkerFactory.getAccountExecutor(coin, accountExecutor, pairExecutor);
        return minedWorker.calculate(walletAddress);
    }

    /**
     * Sends mined result.
     *
     * @param chatId unique identifier for chat
     * @param messageId unique identifier for message
     * @param coin coin type
     * @param pool pool type
     * @param exchanger exchange type
     * @param minedResult mined result
     */
    private void sendMinedResult(Object chatId, Integer messageId, Coin coin, Pool pool, Exchanger exchanger, MinedResult minedResult) {
        String currentBalance = LOCALIZATION.getString("current_balance");
        currentBalance = String.format(currentBalance, pool.getWebsite(), pool.getName().toUpperCase(), minedResult.getCoinsBalance(),
                coin.getWebsite(), coin.getSymbol());
        String minedResultMsg = LOCALIZATION.getString("mined_result");
        minedResultMsg = String.format(minedResultMsg, exchanger.getWebsite(), exchanger.getName().toUpperCase(),
                minedResult.getUsdBalance(), minedResult.getBuyPrice(), minedResult.getSellPrice());
        String formattedMessage = currentBalance + "\n---\n" + minedResultMsg;
        editMessage(chatId, messageId, formattedMessage, true, null);
    }

    /**
     * Sends supporting coins.
     *
     * @param chatId unique identifier for chat
     * @param replyToMessageId unique identifier for reply
     */
    private void sendSupportingCoins(Long chatId, Integer replyToMessageId) {
        List<Coin> coinList = Arrays.asList(Coin.values());
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[coinList.size()];
        for (int i = 0; i < coinList.size(); i++) {
            Coin coin = coinList.get(i);
            String coinSymbol = coin.getSymbol();
            keyboardButtonArray[i] = new InlineKeyboardButton(coinSymbol).callbackData(coinSymbol);
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        sendMessage(chatId, LOCALIZATION.getString("coin"), false, replyToMessageId, keyboardMarkup);
    }

    /**
     * Sends supporting pools.
     *
     * @param chatId unique identifier for chat
     * @param messageId unique identifier for message
     * @param stepData data of current step
     */
    private void sendSupportingPools(Object chatId, Integer messageId, TelegramStepData stepData) {
        List<Pool> poolList = Arrays.asList(Pool.values());
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[poolList.size()];
        for (int i = 0; i < poolList.size(); i++) {
            Pool pool = poolList.get(i);
            String poolName = pool.getName();
            String callbackData = stepData.getCoin().getSymbol() + "_" + poolName;
            keyboardButtonArray[i] = new InlineKeyboardButton(poolName).callbackData(callbackData);
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        editMessage(chatId, messageId, LOCALIZATION.getString("pool"), false, keyboardMarkup);
    }

    /**
     * Sends supporting exchangers.
     *
     * @param chatId unique identifier for chat
     * @param messageId unique identifier for message
     * @param stepData data of current step
     */
    private void sendSupportingExchangers(Object chatId, Integer messageId, TelegramStepData stepData) {
        List<Exchanger> exchangerList = Arrays.asList(Exchanger.values());
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[exchangerList.size()];
        for (int i = 0; i < exchangerList.size(); i++) {
            Exchanger exchanger = exchangerList.get(i);
            String exchangerName = exchanger.getName();
            String callbackData = stepData.getCoin().getSymbol() + "_" + stepData.getPool().getName() + "_" + exchangerName;
            keyboardButtonArray[i] = new InlineKeyboardButton(exchangerName).callbackData(callbackData);
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        editMessage(chatId, messageId, LOCALIZATION.getString("exchanger"), false, keyboardMarkup);
    }

    /**
     * Sends a message.
     *
     * @param chatId unique identifier for chat
     * @param text text of the message
     * @param useHtml use HTML style
     * @param replyToMessageId unique identifier for reply
     * @param replyMarkup additional interface options
     */
    private void sendMessage(Long chatId, String text, boolean useHtml, Integer replyToMessageId, Keyboard replyMarkup) {
        SendMessage request = new SendMessage(chatId, text);
        if (useHtml) {
            request.parseMode(HTML);
        }
        if (replyMarkup != null) {
            request.replyMarkup(replyMarkup);
        }
        if (replyToMessageId != null) {
            request.replyToMessageId(replyToMessageId);
        }
        SendResponse sendResponse = bot.execute(request);
        if (!sendResponse.isOk()) {
            LOG.error(sendResponse.description());
        }
    }

    /**
     * Edits a message.
     *
     * @param chatId unique identifier for chat
     * @param messageId unique identifier for message
     * @param text text of the message
     * @param useHtml use HTML style
     * @param replyMarkup additional interface options
     */
    private void editMessage(Object chatId, Integer messageId, String text, boolean useHtml, InlineKeyboardMarkup replyMarkup) {
        EditMessageText request = new EditMessageText(chatId, messageId, text);
        if (replyMarkup != null) {
            request.replyMarkup(replyMarkup);
        }
        if (useHtml) {
            request.parseMode(HTML);
        }
        BaseResponse response = bot.execute(request);
        if (!response.isOk()) {
            LOG.error(response.description());
        }
    }

}
