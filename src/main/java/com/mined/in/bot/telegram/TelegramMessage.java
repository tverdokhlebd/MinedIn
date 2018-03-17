package com.mined.in.bot.telegram;

import static com.mined.in.bot.telegram.TelegramStepData.Step.EXCHANGER;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
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
    private StringBuilder messageContent;
    /** Error message. */
    private StringBuilder errorMessage;
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
    public void parsePreviousMessage(Message message) {
        InlineKeyboardButton[] keyboardButtonArray = new InlineKeyboardButton[1];
        String callbackQueryData = stepData.getCallbackQueryData();
        keyboardButtonArray[0] = new InlineKeyboardButton(LOCALIZATION.getString("update")).callbackData(callbackQueryData);
        keyboardMarkup = new InlineKeyboardMarkup(keyboardButtonArray);
        parseHtmlMarkup(message);
    }

    /**
     * Gets the final formatted message.
     *
     * @return the final formatted message
     */
    public String getFinalMessage() {
        boolean isFinalStep = stepData != null && stepData.getStep() == EXCHANGER;
        if (isFinalStep) {
            StringBuilder finalMessage = new StringBuilder();
            // messageContent can be null if an error occurred during first calculating
            if (messageContent != null) {
                finalMessage.append(messageContent);
                finalMessage.append("\n-\n");
            }
            String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a Z").format(new Date());
            finalMessage.append(date);
            if (errorMessage != null) {
                finalMessage.append("\n--\n");
                finalMessage.append("<b>");
                finalMessage.append(errorMessage);
                finalMessage.append("</b>");
            }
            return finalMessage.toString();
        } else {
            return messageContent.toString();
        }
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
        this.messageContent = new StringBuilder(messageContent);
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage the new error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = new StringBuilder(errorMessage);
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

    /**
     * Parses HTML markup for message.
     *
     * @param message previous result message
     */
    private void parseHtmlMarkup(Message message) {
        StringBuilder tmpStrBuilder = new StringBuilder(message.text());
        // Position of mined result
        if (tmpStrBuilder.indexOf("\n-\n") != -1) {
            MessageEntity[] entityArray = message.entities();
            int globalOffset = 0;
            for (int i = 0; i < entityArray.length; i++) {
                MessageEntity entity = entityArray[i];
                switch (entity.type()) {
                case text_link: {
                    String urlStart = "<a href=\"" + entity.url() + "\">";
                    tmpStrBuilder.insert(entity.offset() + globalOffset, urlStart);
                    globalOffset += urlStart.length();
                    String urlEnd = "</a>";
                    tmpStrBuilder.insert(entity.offset() + entity.length() + globalOffset, urlEnd);
                    globalOffset += urlEnd.length();
                    break;
                }
                case bold: {
                    String bStart = "<b>";
                    tmpStrBuilder.insert(entity.offset() + globalOffset, bStart);
                    globalOffset += bStart.length();
                    String bEnd = "</b>";
                    tmpStrBuilder.insert(entity.offset() + entity.length() + globalOffset, bEnd);
                    globalOffset += bEnd.length();
                    break;
                }
                default:
                    break;
                }
            }
            messageContent = new StringBuilder(tmpStrBuilder.substring(0, tmpStrBuilder.indexOf("\n-\n")));
        }
    }

}
