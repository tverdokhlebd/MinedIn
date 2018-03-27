package com.mined.in.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mined.in.bot.telegram.TelegramBotUpdates;

/**
 * REST controller for Telegram API.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @Value("${telegram.token}")
    private String telegramToken;

    /**
     * Processes incoming updates from bot.
     *
     * @param token telegram token
     * @param body POST body
     */
    @RequestMapping("/updates/{token}")
    public void updates(@PathVariable("token") String token, @RequestBody String body) {
        if (!telegramToken.equals(token)) {
            return;
        }
        new TelegramBotUpdates(token).process(body);
    }

}
