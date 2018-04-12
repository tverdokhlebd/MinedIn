package com.mined.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web controller.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Controller
public class WebController {

    /**
     * Requests index.html.
     *
     * @return index.html
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

}
