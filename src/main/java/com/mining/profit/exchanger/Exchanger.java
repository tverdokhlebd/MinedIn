package com.mining.profit.exchanger;

import java.math.BigDecimal;

/**
 * Interface for representing of exchanger information like currency pair, balance and etc.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface Exchanger {

    BigDecimal getETHUSDPrice();

    BigDecimal getETHUAHPrice();

}
