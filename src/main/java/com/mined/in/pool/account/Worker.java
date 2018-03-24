package com.mined.in.pool.account;

/**
 * Class for representing pool worker.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Worker {

    /** Worker name. */
    private final String name;
    /** Reported worker hashrate. */
    private final double hashrate;

    /**
     * Creates the pool worker instance.
     *
     * @param name worker name
     * @param hashrate reported worker hashrate
     */
    public Worker(String name, double hashrate) {
        super();
        this.name = name;
        this.hashrate = hashrate;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hashrate.
     *
     * @return the hashrate
     */
    public double getHashrate() {
        return hashrate;
    }

}
