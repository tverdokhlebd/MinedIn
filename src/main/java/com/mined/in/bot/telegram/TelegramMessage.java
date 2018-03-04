package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.EXCHANGER;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * Class for representing telegram message.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramMessage {

    /** Unique identifier of chat. */
    private final Object chatId;
    /** Unique identifier of message. */
    private final Integer messageId;
    /** Message content. */
    private String messageContent;
    /** Error message. */
    private String errorMessage;
    /** Keyboard markup. */
    private InlineKeyboardMarkup keyboardMarkup;
    /** Current step. */
    private TelegramStepData stepData;
    /** Localization resources. */
    private final static ResourceBundle LOCALIZATION = ResourceBundle.getBundle(TelegramUpdatesBot.class.getSimpleName().toLowerCase());

    /**
     * Creates the instance of telegram message.
     *
     * @param chatId unique identifier of chat
     * @param messageId unique identifier of message
     */
    public TelegramMessage(Object chatId, Integer messageId) {
        super();
        this.chatId = chatId;
        this.messageId = messageId;
    }

    /**
     * Parse previous result message.
     *
     * @param message previous result message
     */
    public void parsePreviousMessage(String message) {
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[1];
        String callbackQueryData = stepData.getCallbackQueryData();
        keyboardButtonArray[0] = new InlineKeyboardButton(LOCALIZATION.getString("update")).callbackData(callbackQueryData);
        keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        int minedResultDelimiterIndex = message.indexOf("--");
        if (minedResultDelimiterIndex != -1) {
            messageContent = message.substring(minedResultDelimiterIndex + 2, message.length());
        } else {
            messageContent = "";
        }
    }

    /**
     * Gets the final formatted message.
     *
     * @return the final formatted message
     */
    public String getFinalMessage() {
        String finalMessage = messageContent;
        // It is result message
        if (stepData != null && stepData.getStep() == EXCHANGER) {
            String resultMessage = LOCALIZATION.getString("result");
            if (errorMessage != null) {
                resultMessage = "<b>" + resultMessage + "</b>" + errorMessage;
            } else {
                resultMessage += LOCALIZATION.getString("success");
            }
            String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new Date());
            String lastUpdateMessage = LOCALIZATION.getString("last_update") + date;
            finalMessage = resultMessage + "\n-\n" + lastUpdateMessage + "\n--\n" + finalMessage;
        }
        return finalMessage;
    }

    /**
     * Returns {@code true} if it is necessary to send simple message, otherwise {@code false}.
     *
     * @return {@code true} if it is necessary to send simple message, otherwise {@code false}
     */
    public boolean onlySendMessage() {
        return stepData == null;
    }

    /**
     * Sets the message content.
     *
     * @param messageContent the new message content
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage the new error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the keyboard markup.
     *
     * @return the keyboard markup
     */
    public InlineKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }

    /**
     * Sets the keyboard markup.
     *
     * @param keyboardMarkup the new keyboard markup
     */
    public void setKeyboardMarkup(InlineKeyboardMarkup keyboardMarkup) {
        this.keyboardMarkup = keyboardMarkup;
    }

    /**
     * Gets the step data.
     *
     * @return the step data
     */
    public TelegramStepData getStepData() {
        return stepData;
    }

    /**
     * Sets the step data.
     *
     * @param stepData the new step data
     */
    public void setStepData(TelegramStepData stepData) {
        this.stepData = stepData;
    }

    /**
     * Gets the chat id.
     *
     * @return the chat id
     */
    public Object getChatId() {
        return chatId;
    }

    /**
     * Gets the message id.
     *
     * @return the message id
     */
    public Integer getMessageId() {
        return messageId;
    }

}
