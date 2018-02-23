package com.mining.profit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Main configuration of service.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Entity
@Table(name = "CONFIGURATION")
public class Configuration {

    /** Username of administrator . */
    private String adminUsername;
    /** Telegram token for HTTP API. */
    private String telegramToken;

    /**
     * Creates the main configuration of service.
     */
    public Configuration() {
        super();
    }

    /**
     * Gets the admin username.
     *
     * @return the admin username
     */
    @Id
    @Column(name = "ADMIN_USERNAME", length = 32, nullable = false)
    public String getAdminUsername() {
        return adminUsername;
    }

    /**
     * Gets the telegram token.
     *
     * @return the telegram token
     */
    @Column(name = "TELEGRAM_TOKEN", length = 64)
    public String getTelegramToken() {
        return telegramToken;
    }

    /**
     * Sets the admin username.
     *
     * @param adminUsername the new admin username
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * Sets the telegram token.
     *
     * @param telegramToken the new telegram token
     */
    public void setTelegramToken(String telegramToken) {
        this.telegramToken = telegramToken;
    }

}
