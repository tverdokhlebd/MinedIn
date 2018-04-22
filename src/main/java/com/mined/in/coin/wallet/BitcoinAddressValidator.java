package com.mined.in.coin.wallet;

/**
 * Validator of bitcoin wallet address.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class BitcoinAddressValidator implements AddressValidator {

    @Override
    public boolean isValidAddress(String walletAddress) {
        throw new RuntimeException("No impl");
    }

}
