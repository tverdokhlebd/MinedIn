package com.mined.in.coin.wallet;

/**
 * Interface for validating wallet address.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AddressValidator {

    /**
     * Validates format of wallet address.
     *
     * @param walletAddress wallet address
     * @return {@code true} - if wallet address format is correct, otherwise {@code false}
     */
    boolean isValidAddress(String walletAddress);

}
