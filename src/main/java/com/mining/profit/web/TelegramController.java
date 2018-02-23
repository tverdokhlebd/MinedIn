package com.mining.profit.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Telegram API.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @RequestMapping("/")
    public void telegram() {

    }

}
