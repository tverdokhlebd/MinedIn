package com.mining.profit.bot.telegram;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mining.profit.bot.Bot;
import com.mining.profit.bot.BotCommand;
import com.mining.profit.bot.BotException;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Telegram bot.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramBot implements Bot {

    /** Telegram bot. */
    private final com.pengrad.telegrambot.TelegramBot bot;
    /** Unique identifier of chat. */
    private Long chatId;
    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(TelegramBot.class);
    /** Localization resources. */
    private final static ResourceBundle LOCALIZATION = ResourceBundle.getBundle(TelegramBot.class.getSimpleName().toLowerCase());

    /**
     * Creates the telegram bot.
     *
     * @param token telegram token
     */
    public TelegramBot(String token) {
        super();
        bot = new com.pengrad.telegrambot.TelegramBot(token);
    }

    @Override
    public void process(String request) {
        try {
            Update update = BotUtils.parseUpdate(request);
            Message message = update.message();
            chatId = message.chat().id();
            BotCommand command = parseCommand(message.text());
            switch (command) {
            case START: {
                break;
            }
            }
        } catch (BotException e) {
            sendMessage(e.getMessage());
        }
    }

    private BotCommand parseCommand(String text) throws BotException {
        int indexOfBackslash = text.indexOf("/");
        if (indexOfBackslash == -1 || indexOfBackslash > 0) {
            throw new BotException(LOCALIZATION.getString("not_command"));
        }
        try {
            return BotCommand.valueOf(text.substring(1, text.length()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BotException(LOCALIZATION.getString("wrong_command"));
        }
    }

    private void sendMessage(String message) {
        SendMessage request = new SendMessage(chatId, message).parseMode(HTML);
        SendResponse sendResponse = bot.execute(request);
        if (!sendResponse.isOk()) {
            LOG.error(sendResponse.description());
        }
    }

}
