package com.mined.in.coin.wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator of monero wallet address. Implementation was taken from
 * https://github.com/woodser/monero-wallet-java/blob/master/monero-wallet-java/src/main/java/wallet/MoneroUtils.java.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class MoneroAddressValidator implements AddressValidator {

    private static final int STANDARD_ADDRESS_LENGTH = 95;
    private static final int INTEGRATED_ADDRESS_LENGTH = 106;
    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final List<Character> CHARS = new ArrayList<Character>();
    static {
        for (char c : ALPHABET) {
            CHARS.add(c);
        }
    }

    @Override
    public boolean isValidAddress(String walletAddress) {
        if (walletAddress == null || walletAddress.isEmpty()) {
            return false;
        }
        if (!validateStandardAddress(walletAddress)) {
            return validateIntegratedAddress(walletAddress);
        }
        return true;
    }

    private boolean validateStandardAddress(String standardAddress) {
        if (standardAddress.length() != STANDARD_ADDRESS_LENGTH) {
            return false;
        }
        if (!standardAddress.startsWith("4") && !standardAddress.startsWith("9") && !standardAddress.startsWith("A")) {
            return false;
        }
        try {
            validateBase58(standardAddress);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    private boolean validateIntegratedAddress(String integratedAddress) {
        if (integratedAddress.length() != INTEGRATED_ADDRESS_LENGTH) {
            return false;
        }
        return true;
    }

    private static void validateBase58(String standardAddress) {
        for (char c : standardAddress.toCharArray()) {
            if (!CHARS.contains(c)) {
                throw new RuntimeException("Invalid Base58 " + standardAddress);
            }
        }
    }

}
