/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seleucus.wsp.crypto;


import org.apache.commons.codec.DecoderException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.binary.Hex;
import static java.lang.Math.abs;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author pgolen
 */
public class FwknopSymmetricCryptoTest {
    SecureRandom sr;
    
    public FwknopSymmetricCryptoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        sr = new SecureRandom();
    }
    
    @After
    public void tearDown() {
    }

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
   
   private String removeMAC(String ciphertext, byte hmac_type) {       
        return ciphertext.substring(0, ciphertext.length() - FwknopSymmetricCrypto.DIGEST_BASE64_LENGTH[hmac_type]);
   }

    @Test
    public void shouldVerifyReturnTrueForMD5() {
        String auth_key_encoded = "5e461b35a238fd224b7a0e49755bd0bebdc0276793c80c85d26d22241a8862e7ce6a356ce447ae3da0d92aeda88e35639073b025daea5c0704ad48609e40eb68";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8t14yUfpO+iW9SwPj9iZMwI4f06dmtGaR6LBUib4xEVAafh8QryXxvvbMCLz/xIziBB9LcYU7E3541RDSr+/+G7cYvEaF3POADb9WlTi3PEj8/E2IxSZDH7+gEely9mO2nBxX4iz+O9/n6LyICr5daRd+MjMiB98HMS99/xxbx5V6XzmLp2+P9lMWuhl5bqgWwrw2P4lQpSg";
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_MD5;
        try {
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
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
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA1;
        try {
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
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
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA256;
        try {
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
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
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA384;
        try {
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
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
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA512;
        try {
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyThrowExceptionForInvalidDigestType() {
        String auth_key_encoded = "4c7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "";
        byte hmac_type = 5;
        try {
            FwknopSymmetricCrypto.verify(auth_key, message, hmac_type);
        }
        catch (IllegalArgumentException e) {
            // this should be thrown
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
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_MD5;
        try {
            FwknopSymmetricCrypto.verify(auth_key, message, hmac_type);
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
            String message_with_mac = FwknopSymmetricCrypto.sign(auth_key, message, FwknopSymmetricCrypto.HASH_TYPE_MD5);
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message_with_mac, FwknopSymmetricCrypto.HASH_TYPE_MD5));
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
            String message_with_mac = FwknopSymmetricCrypto.sign(auth_key, message, FwknopSymmetricCrypto.HASH_TYPE_SHA1);
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message_with_mac, FwknopSymmetricCrypto.HASH_TYPE_SHA1));
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
            String message_with_mac = FwknopSymmetricCrypto.sign(auth_key, message, FwknopSymmetricCrypto.HASH_TYPE_SHA256);
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message_with_mac, FwknopSymmetricCrypto.HASH_TYPE_SHA256));
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
            String message_with_mac = FwknopSymmetricCrypto.sign(auth_key, message, FwknopSymmetricCrypto.HASH_TYPE_SHA384);
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message_with_mac, FwknopSymmetricCrypto.HASH_TYPE_SHA384));
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
            String message_with_mac = FwknopSymmetricCrypto.sign(auth_key, message, FwknopSymmetricCrypto.HASH_TYPE_SHA512);
            assertTrue(FwknopSymmetricCrypto.verify(auth_key, message_with_mac, FwknopSymmetricCrypto.HASH_TYPE_SHA512));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }       
    }

    /**
     * Test of getHMACForMessage method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testSign() {
        String auth_key_encoded = "4c7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8Yk+07s9eQPTk8mI227l/kju2QsiQ+mZTipH3y9kFlMwQEPBtIeJFL8UuyHmxz1QsFLoahYBIcCaMsnOaQrLPj9HspsneWcHb0yRv6xUNv4L4L8gfxLLS/8QmpjhGPAUsEUBRf5b9xrw2OnOBgeF2sK6l6A2imFJCA5mBIV4SqjW2NTaVLaQi92y4nEcFaNtC0xDJVUSAvHaXHuUuv8gWiBBvEGQ1mWMzL85Q60Vuzc8Y7Th/SAu/PXFdUJyzbiYbJBSUawoqo/vxS+k0F/rx5Rhzlw+rntkk";
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA512;
        String expResult = message.concat("aec4GabqWJgIpIponKb2e/O6z2p0DREMJIcQgogwVGN2uEtMk33oCZI5N0GzwamhvCSt1jV29ougv9i+JEesEg");
        try {
            String result = FwknopSymmetricCrypto.sign(auth_key, message, hmac_type);
            assertEquals(expResult, result);
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of getIsHMACValid method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testVerify() {
        // Incorrect key, should return false
        String auth_key_encoded = "ac7660e21cf971c70ec8beb622fa966d3ab03ab03a301a2162afa930377737582aae061d0c61590ba2b0453e914d9385cc42250ae6e2cbeca73c8979676da82b";
        byte[] auth_key = decodeFromHexString(auth_key_encoded);
        String message = "8Yk+07s9eQPTk8mI227l/kju2QsiQ+mZTipH3y9kFlMwQEPBtIeJFL8UuyHmxz1QsFLoahYBIcCaMsnOaQrLPj9HspsneWcHb0yRv6xUNv4L4L8gfxLLS/8QmpjhGPAUsEUBRf5b9xrw2OnOBgeF2sK6l6A2imFJCA5mBIV4SqjW2NTaVLaQi92y4nEcFaNtC0xDJVUSAvHaXHuUuv8gWiBBvEGQ1mWMzL85Q60Vuzc8Y7Th/SAu/PXFdUJyzbiYbJBSUawoqo/vxS+k0F/rx5Rhzlw+rntkkaec4GabqWJgIpIponKb2e/O6z2p0DREMJIcQgogwVGN2uEtMk33oCZI5N0GzwamhvCSt1jV29ougv9i+JEesEg";
        byte hmac_type = FwknopSymmetricCrypto.HASH_TYPE_SHA512; 
        try {
            assertFalse(FwknopSymmetricCrypto.verify(auth_key, message, hmac_type));
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of getKeyAndIV method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testDeriveKeyAndIV() {
        byte[] salt = decodeFromHexString("7f910b45532787dc");
        byte[] master_key = decodeFromHexString("4c1dd9b93d5e2ebb5bd10d7f37d0e57db75b283a9f6b19ad74eba776e87689c8");
        byte[] expected_key = decodeFromHexString("82c2f5e606932b5add30dcc5e9dd059c38b971ef45877dd4bc5291c40b1d1d9a");
        byte[] expected_iv = decodeFromHexString("36319896f6cdc575a6e31e4dd691e4f3");

        try {
            byte[][] result = FwknopSymmetricCrypto.deriveKeyAndIV(salt, master_key);
            // should return 2 elements;
            assert(result.length == 2);
            assertArrayEquals(expected_key, result[0]);
            assertArrayEquals(expected_iv, result[1]);
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of encrypt method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testEncrypt() {
        byte[] key = new byte[32];
        sr.nextBytes(key);
        int msgLen = 1 + (abs(sr.nextInt()) % 512);
        byte[] msg = new byte[msgLen];
        String message = new String(msg);
        try {
            String encrypted = FwknopSymmetricCrypto.encrypt(key, message);
            System.out.println(encrypted);
            String decrypted = FwknopSymmetricCrypto.decrypt(key, encrypted);
            assertEquals(message, decrypted);
        }
        catch (InvalidKeyException e) {
            System.out.println("Check: Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files");
            fail ("Unexpected exception: " + e.getMessage());
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of decrypt method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testDecrypt() {
        byte[] key = decodeFromHexString("fd38fb08781e77ca7d85c9f3ec4e35203f4cae3f0d5fd78658638e2d32dd0bc5");
        // ciphertext contains SHA256 HMAC, must be removed before calling decrypt function
        String ciphertext = "88/CLhVNlIRAaqrmMnh0VBwMpoAKZP0r3SwTJ5Rr3PCAVI2xQcDEtnrNnEx6J5udAjWlwtmlCFVGykvLb2X/pXr3G8hf+ZLmQLQV6mU5YHuEqlAlMmXtWZfd65mi5S876hJvdlyhfMpLDrnc5RB/bBPjKpDS98X5fJsDVxnQ7z8LbUYWSsDNt7N2uj4kB6+Ia8usPq5UZIvSoNpNnsPGeyofSC2o6EhfMC9IaiLcfnr54x9cKYw6uApNno5TpNg/3B1dZ9f/DFp48H4fdlxmYehW4h5fPnRPE";
        ciphertext = removeMAC(ciphertext, FwknopSymmetricCrypto.HASH_TYPE_SHA256);
        String expResult = "1183491131188171:dW9wYXJ5b2N1:1421953628:2.0.1:1:MjMyLjIwMC4xMC45NCx0Y3AvNDg4NjY:67Fi6nvavJQAvpaH6OEhqoknCPDd/vf1L0tif8vy1RDE2S67WfTuQ0Fy705ToGN/r9zOwjK8HvqvF6+BY6q7jA";
        try {
            String result = FwknopSymmetricCrypto.decrypt(key, ciphertext);
            assertEquals(expResult, result);
        }
        catch (InvalidKeyException e) {
            System.out.println("Check: Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files");
            fail ("Unexpected exception: " + e.getMessage());
        }
        catch (Exception e) {
            fail ("Unexpected exception: " + e.getMessage());
        }
    }   

    /**
     * Test of equals method, of class FwknopSymmetricCrypto.
     */
    @Test
    public void testEquals() {
        CharSequence a = "Test1";        
        assertTrue(FwknopSymmetricCrypto.equals(a, a));
    }
    
    @Test
    public void shouldEqualsReturnFalseForStringsOfDifferentLength() {
        CharSequence a = "a";
        CharSequence b = "aa";
        assertFalse(FwknopSymmetricCrypto.equals(a, b));
    }
    
    @Test
    public void shouldEqualsReturnFalseForDifferentStrings() {
        CharSequence a = "aaaa";
        CharSequence b = "abcd";
        assertFalse(FwknopSymmetricCrypto.equals(a, b));
    }
}
