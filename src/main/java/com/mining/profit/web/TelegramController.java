package com.mining.profit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mining.profit.bot.TelegramBot;
import com.mining.profit.db.model.Configuration;
import com.mining.profit.db.repository.ConfigurationRepository;

/**
 * REST controller for Telegram API.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @Autowired
    ConfigurationRepository configurationRepository;

    /**
     * Processes incoming updates from bot.
     *
     * @param token telegram token
     * @param body POST body
     */
    @RequestMapping("/updates/{token}")
    public void updates(@PathVariable("token") String token, @RequestBody String body) {
        Configuration configuration = configurationRepository.findAll().get(0);
        if (!configuration.getTelegramToken().equals(token)) {
            return;
        }
        new TelegramBot(token).process(body);
    }

}
