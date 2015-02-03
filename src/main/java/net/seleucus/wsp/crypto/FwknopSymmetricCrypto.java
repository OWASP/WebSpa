/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seleucus.wsp.crypto;

import java.security.InvalidKeyException;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author pgolen
 */
public final class FwknopSymmetricCrypto extends WebSpaUtils {
    
    protected final static byte HASH_TYPE_MD5 = 0;
    protected final static byte HASH_TYPE_SHA1 = 1;
    protected final static byte HASH_TYPE_SHA256 = 2;
    protected final static byte HASH_TYPE_SHA384 = 3;
    protected final static byte HASH_TYPE_SHA512 = 4;
    
    private final static String FWKNOP_ENCRYPTION_HEADER = "U2FsdGVkX1";
    
    private final static byte[] DIGEST_BASE64_LENGTH = {22, 27, 43, 64, 86};
    private final static String[] HMAC_ALGORITHMS = {"HmacMD5", "HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512"};
    
    private FwknopSymmetricCrypto() {
        // Standard to avoid instantiation 'accidents'
        throw new UnsupportedOperationException();
    }
    
    public static byte[] getHMACForMessage(byte[] auth_key, String message, byte hmac_type) {
        // Check if hmac_type is valid
        if (hmac_type > 4 || hmac_type < 0) 
            throw new IllegalArgumentException("Invalid digest type was specified");        

        // Create Mac instance 
        Mac hmac;
        try {
            hmac = Mac.getInstance(HMAC_ALGORITHMS[hmac_type]);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Selected HMAC type is not supported");
        }
        
        // Create key
        SecretKeySpec hmac_key = new SecretKeySpec(auth_key, HMAC_ALGORITHMS[hmac_type]);
        
        // Init hmac object
        try {
            hmac.init(hmac_key);
        }
        catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid key for specified HMAC type");
        }
        
        // Prepare enc_part to calculate HMAC
        byte[] msg_to_hmac = FWKNOP_ENCRYPTION_HEADER.concat(message).getBytes();
        
        // Calculate HMAC and return
        return hmac.doFinal(msg_to_hmac);                
    }
    
    public static boolean getIsHMACValid(byte[] auth_key, String message, byte hmac_type) {
        // Check if hmac_type is valid, get hmac length in base64 encoding
        if (hmac_type > 4 || hmac_type < 0) 
            throw new IllegalArgumentException("Invalid digest type was specified");        
        byte digest_len = DIGEST_BASE64_LENGTH[hmac_type];
        
        // Check if message makes sense (it must be longer than digest length
        if (message.length() <= digest_len) 
            throw new IllegalArgumentException("Message to short");
        
        // Split message into two parts, encrypted payload and HMAC
        String enc_part = message.substring(0, message.length() - digest_len);
        String auth_part = message.substring(message.length() - digest_len, message.length());        
       
        // Calculate HMAC, compare, return results
        return WebSpaUtils.equals(getHMACForMessage(auth_key, enc_part, hmac_type), Base64.decodeBase64(auth_part));
    }
    
}
