package com.mined.in.calculation;

import java.math.BigDecimal;

/**
 * Interface for retrieving mining calculation.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface CalculationExecutor {

    /**
     * Returns ETH mining calculation.
     *
     * @param hashrate reported total hashrate
     * @return ETH mining calculation
     * @throws CalculationExecutorException if there is any error in request executing
     */
    Calculation getETHCalculation(BigDecimal hashrate) throws CalculationExecutorException;

}
