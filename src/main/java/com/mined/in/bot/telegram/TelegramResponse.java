package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.REWARD;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * Class for representing telegram response message.
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
    private final static ResourceBundle RESOURCE = ResourceBundle.getBundle(TelegramBotUpdates.class.getSimpleName().toLowerCase());
    /** Logger. */
    private final static Logger LOG = LoggerFactory.getLogger(TelegramResponse.class);

    /**
     * Creates the instance of telegram response message.
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
        boolean isMessageResult = stepData.getStep() == REWARD;
        String formattedMessage = message;
        if (isMessageResult) {
            if (error != null && message == null) {
                message = RESOURCE.getString("no_result");
            }
            String currentDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a Z").format(new Date());
            int maxStrLength = getStringMaxLength(message, currentDate);
            StringBuilder resultMessage = new StringBuilder();
            resultMessage.append(insertSeparatorsAndPreTag(message, maxStrLength));
            resultMessage.append("\n");
            String formattedDate = String.format(RESOURCE.getString("last_update"), currentDate);
            resultMessage.append(insertSeparatorsAndPreTag(formattedDate, maxStrLength));
            if (error != null) {
                resultMessage.append("\n");
                resultMessage.append(error);
            }
            formattedMessage = resultMessage.toString();
        }
        LOG.debug("Formatted message: {}", formattedMessage);
        return formattedMessage;
    }

    /**
     * Returns {@code true} if it is necessary to send message, otherwise {@code false}.
     *
     * @return {@code true} if it is necessary to send message, otherwise {@code false}
     */
    public boolean onlySendMessage() {
        TelegramStepData.Step currentStep = stepData.getStep();
        return currentStep == TelegramStepData.Step.START || currentStep == TelegramStepData.Step.WALLET;
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
        // Position of result message beginning
        if (messageBuilder.indexOf("-\n") != -1) {
            MessageEntity[] entityArray = resultMessage.entities();
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
            LOG.debug("Parsed message: {}", message);
        }
    }

    /**
     * Returns length of string with maximum number of symbols.
     *
     * @param message result message
     * @param date current date
     * @return length of string with maximum number of symbols
     */
    private int getStringMaxLength(String message, String date) {
        int messageMaxStrLength = Arrays.asList(message.split("\n")).stream().mapToInt(value -> value.length()).max().getAsInt();
        return messageMaxStrLength > date.length() ? messageMaxStrLength : date.length();
    }

    /**
     * Inserts separators and "pre" tag.
     *
     * @param message result message
     * @param separatorLength length of separator
     * @return result message with separators and "pre" tag
     */
    private String insertSeparatorsAndPreTag(String message, int separatorLength) {
        String separator = String.format("%-" + separatorLength + "s", "-").replace(" ", "-");
        message = message.replaceAll("<separator>", separator);
        message = "<pre>" + message + "</pre>";
        return message;
    }

}
