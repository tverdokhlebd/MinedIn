package com.mined.in.coin.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests of ethereum address validator.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class EthereumAddressValidatorTest {

    @Test
    public void testCorrectAddresses() {
        AddressValidator validator = new EthereumAddressValidator();
        String walletAddress1 = "4e2c24519354a63c37869d04cefb7d113d17fdc3";
        assertEquals(true, validator.isValidAddress(walletAddress1));
        String walletAddress2 = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";
        assertEquals(true, validator.isValidAddress(walletAddress2));
        String walletAddress3 = "4E2C24519354A63C37869D04CEFB7D113D17FDC3";
        assertEquals(true, validator.isValidAddress(walletAddress3));
        String walletAddress4 = "0X4E2C24519354A63C37869D04CEFB7D113D17FDC3";
        assertEquals(true, validator.isValidAddress(walletAddress4));
    }

    @Test
    public void testIncorrectAddresses() {
        AddressValidator validator = new EthereumAddressValidator();
        String walletAddress1 = "4e2c24519354a63c37869d04cefb7d113d";
        assertEquals(false, validator.isValidAddress(walletAddress1));
        String walletAddress2 = "0x4e2c24519354a63c37869d04cefb7d113d";
        assertEquals(false, validator.isValidAddress(walletAddress2));
        String walletAddress3 = "4E2C24519354A63C37869D04CEFB7D113D";
        assertEquals(false, validator.isValidAddress(walletAddress3));
        String walletAddress4 = "0X4E2C24519354A63C37869D04CEFB7D113D";
        assertEquals(false, validator.isValidAddress(walletAddress4));
        String walletAddress5 = "4E2C24519354A63C37869D04CEFB7D113D17FDC3333333";
        assertEquals(false, validator.isValidAddress(walletAddress5));
        String walletAddress6 = "0X4E2C24519354A63C37869D04CEFB7D113D17FDC3333333";
        assertEquals(false, validator.isValidAddress(walletAddress6));
    }

    @Test
    public void testEmptyAddresses() {
        AddressValidator validator = new EthereumAddressValidator();
        String walletAddress1 = "";
        assertEquals(false, validator.isValidAddress(walletAddress1));
        String walletAddress2 = null;
        assertEquals(false, validator.isValidAddress(walletAddress2));
    }

}
