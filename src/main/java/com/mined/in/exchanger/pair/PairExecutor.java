package com.mined.in.exchanger.pair;

/**
 * Interface for retrieving pair from exchanger.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface PairExecutor {

    /**
     * Returns ETH/USD pair.
     *
     * @return ETH/USD pair
     * @throws PairExecutorException if there is any error in request executing
     */
    Pair getETHUSDPair() throws PairExecutorException;

}
