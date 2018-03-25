package com.mined.in.calculator;

import java.math.BigDecimal;

/**
 * Interface for retrieving mining calculation.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface CalculationExecutor {

    /**
     * Returns ethereum mining calculation.
     *
     * @param hashrate reported total hashrate
     * @return ethereum mining calculation
     * @throws CalculationExecutorException if there is any error in request executing
     */
    Calculation getEthereumCalculation(BigDecimal hashrate) throws CalculationExecutorException;

}
