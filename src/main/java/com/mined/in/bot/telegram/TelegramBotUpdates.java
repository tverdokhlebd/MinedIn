package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.CALCULATION;
import static com.mined.in.calculation.CalculationType.WHAT_TO_MINE;
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
import com.mined.in.calculation.Calculation;
import com.mined.in.calculation.CalculationExecutor;
import com.mined.in.calculation.CalculationExecutorException;
import com.mined.in.calculation.CalculationExecutorFactory;
import com.mined.in.calculation.CalculationType;
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
import com.pengrad.telegrambot.TelegramBot;
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
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle(TelegramBotUpdates.class.getSimpleName().toLowerCase());
    /** Terahash. */
    private final static BigDecimal TERAHASH = BigDecimal.valueOf(1_000_000_000_000L);

    /**
     * Creates the instance for processing incoming updates from Telegram bot.
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
                stepData.setCalculationType(WHAT_TO_MINE);
                stepData.setStep(CALCULATION);
            }
            case MARKET:
            case CALCULATION: {
                responseMessage.parsePreviousResultMessage(incomingMessage);
                String walletAddress = incomingMessage.replyToMessage().text();
                MinedResult minedResult = calculateMined(walletAddress);
                createMinedResultMessage(minedResult);
                break;
            }
            }
        } catch (AccountExecutorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("pool_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (MarketExecutorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("market_error"), e.getMessage()));
            LOG.error("Incoming updates processing error", e);
        } catch (CalculationExecutorException e) {
            responseMessage.setError(String.format(RESOURCE.getString("mining_calculation_error"), e.getMessage()));
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
        responseMessage.setKeyboardMarkup(new InlineKeyboardMarkup(keyboardButtonArray));
        responseMessage.setMessage(RESOURCE.getString("select_coin"));
    }

    /**
     * Creates supporting pools message.
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
     * Calculates mined.
     *
     * @param walletAddress wallet address
     * @return mined result
     * @throws AccountExecutorException if there is any error in account creating
     * @throws MarketExecutorException if there is any error in market creating
     * @throws CalculationExecutorException if there is any error in mining calculation creating
     */
    private MinedResult calculateMined(String walletAddress)
            throws AccountExecutorException, MarketExecutorException, CalculationExecutorException {
        TelegramStepData stepData = responseMessage.getStepData();
        CoinType coinType = stepData.getCoinType();
        PoolType poolType = stepData.getPoolType();
        MarketType marketType = stepData.getMarketType();
        CalculationType calculationType = stepData.getCalculationType();
        AccountExecutor accountExecutor = AccountExecutorFactory.getAccountExecutor(poolType);
        MarketExecutor marketExecutor = MarketExecutorFactory.getMarketExecutor(marketType);
        CalculationExecutor calculationExecutor = CalculationExecutorFactory.getCalculationExecutor(calculationType);
        MinedWorker minedWorker = MinedWorkerFactory.getMinedWorker(coinType, accountExecutor, marketExecutor, calculationExecutor);
        return minedWorker.calculate(walletAddress);
    }

    /**
     * Creates mined result message.
     *
     * @param minedResult mined result
     */
    private void createMinedResultMessage(MinedResult minedResult) {
        TelegramStepData stepData = responseMessage.getStepData();
        BigDecimal coinBalance = minedResult.getCoinBalance().setScale(8, DOWN);
        BigDecimal usdBalance = minedResult.getUsdBalance().setScale(2, DOWN);
        BigDecimal coinPrice = minedResult.getCoinPrice().setScale(2, DOWN);
        String balanceMessage = RESOURCE.getString("balance");
        balanceMessage = String.format(balanceMessage,
                                       "$" + usdBalance,
                                       "$" + coinPrice);
        Calculation calculation = minedResult.getCalculation();
        String accountMessage = RESOURCE.getString("account");
        accountMessage = String.format(accountMessage,
                                       stepData.getPoolType().getName(),
                                       coinBalance + " " + stepData.getCoinType().getSymbol(),
                                       stepData.getPoolType().getName(),
                                       calculation.getTotalHashrate().setScale(2, DOWN) + " MH/s");
        BigDecimal perHour = calculation.getRewardPerHour().setScale(6, DOWN);
        BigDecimal perDay = calculation.getRewardPerDay().setScale(6, DOWN);
        BigDecimal perWeek = calculation.getRewardPerWeek().setScale(6, DOWN);
        BigDecimal perMonth = calculation.getRewardPerMonth().setScale(6, DOWN);
        BigDecimal perYear = calculation.getRewardPerYear().setScale(6, DOWN);
        String rewardsMessage = RESOURCE.getString("rewards");
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
                                       calculation.getBlockTime().toPlainString() + "s",
                                       calculation.getBlockCount(),
                                       calculation.getDifficulty(),
                                       calculation.getNetworkHashrate().divide(TERAHASH, 2, DOWN).toPlainString() + " TH/s");
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