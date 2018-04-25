package com.mined.in.coin.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests of monero address validator.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@SpringBootTest
public class MoneroAddressValidatorTest {

    @Test
    public void testCorrectAddresses() {
        AddressValidator validator = new MoneroAddressValidator();
        String walletAddress1 = "48YQFWyu8mUFYHLvqvXvHF4t8YuGrjZs7JSSjrzb6onmWEm8BiP2mWBCrtf81Dsn3yChLipdyDWkejFS9ogKm9ug8ATPYek";
        assertEquals(true, validator.isValidAddress(walletAddress1));
        String walletAddress2 =
                "4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE1B";
        assertEquals(true, validator.isValidAddress(walletAddress2));
    }

    @Test
    public void testIncorrectAddresses() {
        AddressValidator validator = new MoneroAddressValidator();
        String walletAddress1 = "48YQFWyu8mUFYHLvqvXvHF4t8YuGrjZs7JSSjrzb6onmWEm8BiP2mWBCrtf81Dsn3yChLipdyDWkejFS9ogKm9ug8ATPY";
        assertEquals(false, validator.isValidAddress(walletAddress1));
        String walletAddress2 = "4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE";
        assertEquals(false, validator.isValidAddress(walletAddress2));
    }

    @Test
    public void testEmptyAddresses() {
        AddressValidator validator = new MoneroAddressValidator();
        String walletAddress1 = "";
        assertEquals(false, validator.isValidAddress(walletAddress1));
        String walletAddress2 = null;
        assertEquals(false, validator.isValidAddress(walletAddress2));
    }

}
