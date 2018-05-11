package com.mined.in.bot.telegram;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * Telegram response message.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TelegramResponse {

    /** Data of current step. */
    private TelegramStepData stepData;
    /** Result message. */
    private String message;
    /** Error message. */
    private String error;
    /** Keyboard markup. */
    private InlineKeyboardMarkup keyboardMarkup;
    /** Text resources. */
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("bot");

    /**
     * Creates the instance.
     *
     * @param stepData data of current step
     */
    public TelegramResponse(TelegramStepData stepData) {
        super();
        this.stepData = stepData;
    }

    /**
     * Parse previous result message.
     *
     * @param resultMessage previous result message
     */
    public void parsePreviousResultMessage(Message resultMessage) {
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[1];
        String callbackQueryData = stepData.getCallbackQueryData();
        keyboardButtonArray[0] = new InlineKeyboardButton(RESOURCE.getString("update")).callbackData(callbackQueryData);
        keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        parseHtmlMarkup(resultMessage);
    }

    /**
     * Gets the formatted message.
     *
     * @return the formatted message
     */
    public String getFormattedMessage() {
        StringBuilder resultMessage = new StringBuilder();
        switch (stepData.getStep()) {
        case START:
            createSimpleResultMessage(resultMessage);
            break;
        case ENTER_WALLET:
            createSimpleResultMessage(resultMessage);
            break;
        case SELECT_COIN_TYPE:
            createSimpleResultMessage(resultMessage);
            break;
        case SELECT_POOL_ACCOUNT:
            break;
        case SELECT_COIN_INFO:
            break;
        case SELECT_COIN_MARKET:
            break;
        case SELECT_COIN_REWARD: {
            boolean firstMessageWithError = error != null && message == null;
            if (firstMessageWithError) {
                message = String.format(RESOURCE.getString("no_result"));
            }
            resultMessage.append("<pre>" + message + "</pre>");
            resultMessage.append("\n");
            String formattedDate = String.format(RESOURCE.getString("last_update"), getCurrentDate());
            resultMessage.append("<pre>" + formattedDate + "</pre>");
            if (error != null) {
                resultMessage.append("\n");
                resultMessage.append(error);
            }
            break;
        }
        }
        return resultMessage.toString();
    }

    /**
     * Returns {@code true} if it is necessary to send message, otherwise {@code false}.
     *
     * @return {@code true} if it is necessary to send message, otherwise {@code false}
     */
    public boolean onlySendMessage() {
        TelegramStepData.Step currentStep = stepData.getStep();
        return currentStep == TelegramStepData.Step.START || currentStep == TelegramStepData.Step.ENTER_WALLET;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the error.
     *
     * @param error the new error
     */
    public void setError(String error) {
        this.error = error;
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
     * Parses HTML markup for message.
     *
     * @param resultMessage previous result message
     */
    private void parseHtmlMarkup(Message resultMessage) {
        StringBuilder messageBuilder = new StringBuilder(resultMessage.text());
        MessageEntity[] entityArray = resultMessage.entities();
        if (entityArray != null) {
            int globalOffset = 0;
            String preStart = "<pre>";
            String preEnd = "</pre>";
            for (int i = 0; i < entityArray.length; i++) {
                MessageEntity entity = entityArray[i];
                switch (entity.type()) {
                case pre: {
                    messageBuilder.insert(entity.offset() + globalOffset, preStart);
                    globalOffset += preStart.length();
                    messageBuilder.insert(entity.offset() + entity.length() + globalOffset, preEnd);
                    globalOffset += preEnd.length();
                    break;
                }
                default:
                    break;
                }
            }
            int preStartIndex = messageBuilder.indexOf(preStart);
            int preEndIndex = messageBuilder.indexOf(preEnd);
            if (preStartIndex != -1 && preEndIndex != -1) {
                message = messageBuilder.substring(preStartIndex + preStart.length(), preEndIndex);
            }
        }
    }

    /**
     * Gets current date with set user time zone.
     *
     * @return current date with set user time zone
     */
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    /**
     * Creates simple result message without "Update" button.
     *
     * @param resultMessage result message builder
     */
    private void createSimpleResultMessage(StringBuilder resultMessage) {
        if (error != null) {
            resultMessage.append("\n");
            resultMessage.append(error);
        } else {
            resultMessage.append(message);
        }
    }

}
