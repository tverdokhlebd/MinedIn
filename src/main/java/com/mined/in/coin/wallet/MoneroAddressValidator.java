package com.mined.in.coin.wallet;

/**
 * Validator of monero wallet address.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MoneroAddressValidator implements AddressValidator {

    @Override
    public boolean isValidAddress(String walletAddress) {
        throw new RuntimeException("No impl");
    }

}
