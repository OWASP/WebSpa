package net.seleucus.wsp.crypto.fwknop;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.security.SecureRandom;

import static java.lang.Math.abs;
import static net.seleucus.wsp.crypto.fwknop.MessageBuilder.createMessage;
import static net.seleucus.wsp.crypto.fwknop.fields.DigestType.SHA256;
import static net.seleucus.wsp.crypto.fwknop.fields.EncryptionMode.CBC;
import static net.seleucus.wsp.crypto.fwknop.fields.EncryptionType.AES;
import static net.seleucus.wsp.crypto.fwknop.fields.HmacType.*;
import static net.seleucus.wsp.crypto.fwknop.fields.MessageType.AccessMessage;
import static net.seleucus.wsp.crypto.fwknop.fields.Version.CURRENT;
import static org.junit.Assert.*;

/**
 *
 * @author pgolen
 */
public class FwknopSymmetricCryptoServiceTest {
    
    private final SecureRandom sr = new SecureRandom();
    private final FwknopSymmetricCryptoService service = new FwknopSymmetricCryptoService(sr, AES, CBC, SHA256);

   private static byte[] decodeFromHexString(String key) {
       byte[] returnValue = null;
       try {
           returnValue = Hex.decodeHex(key.toCharArray());
       }
       catch (DecoderException e) {
           System.out.println("Incorrect key passed to test");
       }
       return returnValue;         
   }

    @Test
    public void shouldVerifyReturnTrueForMD5() {
        String auth_key_encoded = "5e461b35a238fd224b7a0e49755bd0bebdc0276793c80c85d26d22241a8862e7ce6a356ce447ae3da0d92aeda88e35639073b025daea5c0704ad48609e40eb68";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8t14yUfpO+iW9SwPj9iZMwI4f06dmtGaR6LBUib4xEVAafh8QryXxvvbMCLz/xIziBB9LcYU7E3541RDSr+/+G7cYvEaF3POADb9WlTi3PEj8/E2IxSZDH7+gEely9mO2nBxX4iz+O9/n6LyICr5daRd+MjMiB98HMS99/xxbx5V6XzmLp2+P9lMWuhl5bqgWwrw2P4lQpSg";

        try {
            assertTrue(service.verify(auth_key, message, HmacMD5));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyReturnTrueForSHA1() {
        String auth_key_encoded = "557a6c8cf955b43e8373ebbce50bc27e6e1fdef88730529d6de7f9c53b8fc027076cf22ef9ea1aa385e3ecb05beb52b0faba4a19fe389e9ba79890b1e501f2c2";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "+UAbs68vJsWnN+SfC6eR0LC43LFTBjzrm5wUo9HKNUIZDK9zAsDTN7HaZHiG4y4CPbERRI9IKRCspvRzE7n3LpG8qIk3RRHQXsh/b5iozFKYgFsPhE5N6aBVpj1T6bRiRPsAJoMdpVyCBHtIXb5LxQpNuNE4+muknL9K2bpE+epny7bXAzZeGbIRYLJacaJDx+UvMuO3wl+w1KvPbBNaCST+XQIpyYHtBpz7T0k";

        try {
            assertTrue(service.verify(auth_key, message, HmacSHA1));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    
    @Test
    public void shouldVerifyReturnTrueForSHA256() {
        String auth_key_encoded = "7ff868cedeedc51b37cd1ce3e28cd23a648641ff9a421ad1f6fc435a716ec47a1f67fe7a7e42fe256e4968b693aebd2108fe682e75460a46115eb9ececf9e6db";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "88/CLhVNlIRAaqrmMnh0VBwMpoAKZP0r3SwTJ5Rr3PCAVI2xQcDEtnrNnEx6J5udAjWlwtmlCFVGykvLb2X/pXr3G8hf+ZLmQLQV6mU5YHuEqlAlMmXtWZfd65mi5S876hJvdlyhfMpLDrnc5RB/bBPjKpDS98X5fJsDVxnQ7z8LbUYWSsDNt7N2uj4kB6+Ia8usPq5UZIvSoNpNnsPGeyofSC2o6EhfMC9IaiLcfnr54x9cKYw6uApNno5TpNg/3B1dZ9f/DFp48H4fdlxmYehW4h5fPnRPE";

        try {
            assertTrue(service.verify(auth_key, message, HmacSHA256));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyReturnTrueForSHA384() {
        String auth_key_encoded = "5f9b42b9cbe10d7725b054983dae9223ff9db44c42bb0978f76c6eb392834d751447c78febf8e21e10ccf113ccbcfd9839f832b4dd3496a0f2de05b175dba681";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8gjrQ7ywV7MBH4UZDEnLqZ/DnjPMJgXuRtR3JtxAW4HQPGG7LlGUE1A8PDDM0qg4D2aP/PuDMrCGiVPxVpVqprlPLVn/hCGPV4m2cNchdj2R5LArXBF7AJ3scEmHceo6ZaUjL3rMzGbvANy8RP0AJ2jsflicNFPv8W50yPmEemc2SiLB+PIN7mOVzRwhfcaONcJr7T1sAauzQM6H4a55WfpVyVMhwYJCn";

        try {
            assertTrue(service.verify(auth_key, message, HmacSHA384));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyReturnTrueForSHA512() {
        String auth_key_encoded = "4c7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8Yk+07s9eQPTk8mI227l/kju2QsiQ+mZTipH3y9kFlMwQEPBtIeJFL8UuyHmxz1QsFLoahYBIcCaMsnOaQrLPj9HspsneWcHb0yRv6xUNv4L4L8gfxLLS/8QmpjhGPAUsEUBRf5b9xrw2OnOBgeF2sK6l6A2imFJCA5mBIV4SqjW2NTaVLaQi92y4nEcFaNtC0xDJVUSAvHaXHuUuv8gWiBBvEGQ1mWMzL85Q60Vuzc8Y7Th/SAu/PXFdUJyzbiYbJBSUawoqo/vxS+k0F/rx5Rhzlw+rntkkaec4GabqWJgIpIponKb2e/O6z2p0DREMJIcQgogwVGN2uEtMk33oCZI5N0GzwamhvCSt1jV29ougv9i+JEesEg";

        try {
            assertTrue(service.verify(auth_key, message, HmacSHA512));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void shouldVerifyThrowExceptionForToShortMessage() {
        String auth_key_encoded = "4c7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "YWFhYWFhYWFhYWFhYWFhYW";

        try {
            service.verify(auth_key, message, HmacMD5);
        }
        catch (IllegalArgumentException e) {
            // this should be thrown
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void shouldSignAndVerifyWithMD5ReturnTrue() {
        byte[] auth_key = new byte[64];
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        
        sr.nextBytes(auth_key);
        sr.nextBytes(msg);
        
        String message = Base64.encodeBase64String(msg);
        
        try {
            String message_with_mac = service.sign(auth_key, message, HmacMD5);
            assertTrue(service.verify(auth_key, message_with_mac, HmacMD5));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }
    
    @Test
    public void shouldSignAndVerifyWithSHA1ReturnTrue() {
        byte[] auth_key = new byte[64];
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        
        sr.nextBytes(auth_key);
        sr.nextBytes(msg);
        
        String message = Base64.encodeBase64String(msg);
        
        try {
            String message_with_mac = service.sign(auth_key, message, HmacSHA1);
            assertTrue(service.verify(auth_key, message_with_mac, HmacSHA1));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }
    
    @Test
    public void shouldSignAndVerifyWithSHA256ReturnTrue() {
        byte[] auth_key = new byte[64];
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        
        sr.nextBytes(auth_key);
        sr.nextBytes(msg);
        
        String message = Base64.encodeBase64String(msg);
        
        try {
            String message_with_mac = service.sign(auth_key, message, HmacSHA256);
            assertTrue(service.verify(auth_key, message_with_mac, HmacSHA256));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }
    
    @Test
    public void shouldSignAndVerifyWithSHA384ReturnTrue() {
        byte[] auth_key = new byte[64];
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        
        sr.nextBytes(auth_key);
        sr.nextBytes(msg);
        
        String message = Base64.encodeBase64String(msg);
        
        try {
            String message_with_mac = service.sign(auth_key, message, HmacSHA384);
            assertTrue(service.verify(auth_key, message_with_mac, HmacSHA384));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }
    
    @Test
    public void shouldSignAndVerifyWithSHA512ReturnTrue() {
        byte[] auth_key = new byte[64];
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        
        sr.nextBytes(auth_key);
        sr.nextBytes(msg);
        
        String message = Base64.encodeBase64String(msg);
        
        try {
            String message_with_mac = service.sign(auth_key, message, HmacSHA512);
            assertTrue(service.verify(auth_key, message_with_mac, HmacSHA512));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }

    /**
     * Test of getHMACForMessage method, of class FwknopSymmetricCryptoService.
     */
    @Test
    public void testSign() {
        String auth_key_encoded = "4c7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8Yk+07s9eQPTk8mI227l/kju2QsiQ+mZTipH3y9kFlMwQEPBtIeJFL8UuyHmxz1QsFLoahYBIcCaMsnOaQrLPj9HspsneWcHb0yRv6xUNv4L4L8gfxLLS/8QmpjhGPAUsEUBRf5b9xrw2OnOBgeF2sK6l6A2imFJCA5mBIV4SqjW2NTaVLaQi92y4nEcFaNtC0xDJVUSAvHaXHuUuv8gWiBBvEGQ1mWMzL85Q60Vuzc8Y7Th/SAu/PXFdUJyzbiYbJBSUawoqo/vxS+k0F/rx5Rhzlw+rntkk";

        String expResult = message.concat("aec4GabqWJgIpIponKb2e/O6z2p0DREMJIcQgogwVGN2uEtMk33oCZI5N0GzwamhvCSt1jV29ougv9i+JEesEg");
        try {
            String result = service.sign(auth_key, message, HmacSHA512);
            assertEquals(expResult, result);
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of getIsHMACValid method, of class FwknopSymmetricCryptoService.
     */
    @Test
    public void testVerify() {
        // Incorrect key, should return false
        String auth_key_encoded = "ac7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8Yk+07s9eQPTk8mI227l/kju2QsiQ+mZTipH3y9kFlMwQEPBtIeJFL8UuyHmxz1QsFLoahYBIcCaMsnOaQrLPj9HspsneWcHb0yRv6xUNv4L4L8gfxLLS/8QmpjhGPAUsEUBRf5b9xrw2OnOBgeF2sK6l6A2imFJCA5mBIV4SqjW2NTaVLaQi92y4nEcFaNtC0xDJVUSAvHaXHuUuv8gWiBBvEGQ1mWMzL85Q60Vuzc8Y7Th/SAu/PXFdUJyzbiYbJBSUawoqo/vxS+k0F/rx5Rhzlw+rntkkaec4GabqWJgIpIponKb2e/O6z2p0DREMJIcQgogwVGN2uEtMk33oCZI5N0GzwamhvCSt1jV29ougv9i+JEesEg";
        try {
            assertFalse(service.verify(auth_key, message, HmacSHA512));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of getKeyAndIV method, of class FwknopSymmetricCryptoService.
     */
    @Test
    public void testDeriveKeyAndIV() {
        byte[] salt = decodeFromHexString("7f910b45532787dc");
        byte[] master_key = decodeFromHexString("4c1dd9b93d5e2ebb5bd10d7f37d0e57db75b283a9f6b19ad74eba776e87689c8");
        byte[] expected_key = decodeFromHexString("82c2f5e606932b5add30dcc5e9dd059c38b971ef45877dd4bc5291c40b1d1d9a");
        byte[] expected_iv = decodeFromHexString("36319896f6cdc575a6e31e4dd691e4f3");

        try {
            MessageKey messageKey = service.deriveKeyAndIV(salt, master_key);
            // should return 2 elements;
            assertArrayEquals(expected_key, messageKey.key());
            assertArrayEquals(expected_iv, messageKey.initialisationVector());
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of equals method, of class FwknopSymmetricCryptoService.
     */
    @Test
    public void testEquals() {
        CharSequence a = "Test1";        
        assertTrue(service.equals(a, a));
    }
    
    @Test
    public void shouldEqualsReturnFalseForStringsOfDifferentLength() {
        CharSequence a = "a";
        CharSequence b = "aa";
        assertFalse(service.equals(a, b));
    }
    
    @Test
    public void shouldEqualsReturnFalseForDifferentStrings() {
        CharSequence a = "aaaa";
        CharSequence b = "abcd";
        assertFalse(service.equals(a, b));
    }

    /**
     * Test taken from the following command line execution of fwknop:
     *
     *   $ fwknop -A tcp/80 -a 1.1.1.1 -D 2.2.2.2 -T --hmac-digest-type sha256
     *
     * This produced the following output which has been used in this test to provide expected values:
     *
     *  Encryption key entered = "ENCRYPTION_KEY"
     *  HMAC key entered = "SIGNING_KEY"
     *
     *
     *  SPA Field Values:
     *  =================
     *
     *     Random Value: 1662754693713426
     *         Username: imberda
     *        Timestamp: 1424728557
     *      FKO Version: 2.0.2
     *     Message Type: 1 (Access msg)
     *   Message String: 1.1.1.1,tcp/80
     *       Nat Access: <NULL>
     *      Server Auth: <NULL>
     *   Client Timeout: 0
     *      Digest Type: 3 (SHA256)
     *        HMAC Type: 3 (SHA256)
     *  Encryption Type: 1 (Rijndael)
     *  Encryption Mode: 2 (CBC)
     *     Encoded Data: 1662754693713426:aW1iZXJkYQ:1424728557:2.0.2:1:MS4xLjEuMSx0Y3AvODA
     *  SPA Data Digest: jEscqvxrSt7+Lb2cQ+ICwgX4mhuSp86P8XikxSbga1s
     *             HMAC: Kn75C+V+O0xu3nzVvvdteuHk0zBptmFep8LQD/JgfWo
     *  Final SPA Data: 9sPsHAzpiUCLultUmkjizcRSb0eIkvlXLq9noQ4WbXAT3HlZhWLHoKGAM+TOOtIMxRJ6sOSAlzhx6wNgCQiZ3msYcNslJC0F9xxVYVhw8kNon4uuzXoZyCBb3g9m+nZVVyRyy3f0UO+eljC9WRHTsRs5m6Qlryzec
     *
     * @throws Exception
     */
    @Test
    public void shouldEncryptAndDecrypt() throws Exception {

        final byte[] encryptKey = "ENCRYPTION_KEY".getBytes();

        final Message message = createMessage()
                .withMessageType(AccessMessage)
                .withRandomValue(1662754693713426L)
                .withUsername("imberda")
                .withVersion(CURRENT)
                .withTimestamp(1424728557)
                .withPayload("1.1.1.1,tcp/80")
                .build();

        final String expectedEncodedMessage = "1662754693713426:aW1iZXJkYQ:1424728557:2.0.2:1:MS4xLjEuMSx0Y3AvODA";
        assertEquals(expectedEncodedMessage, message.encoded());

        final byte[] fixedSalt = new byte[]{108, 62, -63, -64, -50, -104, -108, 8};

        final String encryptedMessage = service.encrypt(encryptKey, fixedSalt, message);

        final String expectedEncryptedMessage = "9sPsHAzpiUCLultUmkjizcRSb0eIkvlXLq9noQ4WbXAT3HlZhWLHoKGAM+TOOtIMxRJ6sOSAlzhx6wNgCQiZ3msYcNslJC0F9xxVYVhw8kNon4uuzXoZyC";
        assertEquals(expectedEncryptedMessage, encryptedMessage);

        final String decryptedMessage = service.decrypt(encryptKey, encryptedMessage);
        final String expectedDigestPadding = ":jEscqvxrSt7+L";
        assertEquals(expectedEncodedMessage + expectedDigestPadding, decryptedMessage);
    }
}
