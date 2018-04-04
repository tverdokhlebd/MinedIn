package com.mined.in.coin.wallet;

/**
 * Validator of ethereum wallet address.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthereumAddressValidator implements AddressValidator {

    /** Validate format. */
    private static final String FORMAT = "^[0-9a-f]{40}$";

    @Override
    public boolean isValidAddress(String walletAddress) {
        if (walletAddress == null || walletAddress.isEmpty()) {
            return false;
        }
        walletAddress = walletAddress.toLowerCase().replace("0x", "");
        return walletAddress.matches(FORMAT);
    }

}
