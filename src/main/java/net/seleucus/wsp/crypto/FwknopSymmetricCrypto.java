/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seleucus.wsp.crypto;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

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
    private final static byte SALT_LEN = 8;
    private final static byte IV_LEN = 16;
    private final static byte KEY_LEN = 32;
    
    protected final static byte[] DIGEST_BASE64_LENGTH = {22, 27, 43, 64, 86};
    private final static String[] HMAC_ALGORITHMS = {"HmacMD5", "HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512"};
    
    private FwknopSymmetricCrypto() {
        // Standard to avoid instantiation 'accidents'
        throw new UnsupportedOperationException();
    }
    
    public static String sign(byte[] auth_key, String message, byte hmac_type) throws NoSuchAlgorithmException, InvalidKeyException {
        // Check if hmac_type is valid
        if (hmac_type > 4 || hmac_type < 0) 
            throw new IllegalArgumentException("Invalid digest type was specified");        

        // Create Mac instance 
        Mac hmac;
        hmac = Mac.getInstance(HMAC_ALGORITHMS[hmac_type]);
        
        // Create key
        SecretKeySpec hmac_key = new SecretKeySpec(auth_key, HMAC_ALGORITHMS[hmac_type]);
        
        // Init hmac object
        hmac.init(hmac_key);
        
        // Prepare enc_part to calculate HMAC
        byte[] msg_to_hmac = FWKNOP_ENCRYPTION_HEADER.concat(message).getBytes();
        
        // Calculate HMAC and return
        return message.concat(Base64.encodeBase64String(hmac.doFinal(msg_to_hmac)).replace("=", ""));
    }        
    
    public static boolean verify(byte[] auth_key, String message, byte hmac_type) throws NoSuchAlgorithmException, InvalidKeyException {
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
        return equals(sign(auth_key, enc_part, hmac_type), message);
    }
    
    protected static boolean equals(CharSequence a, CharSequence b) {
        if (a.length() != b.length())
            return false;
        boolean result = true;
        for (int i=0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                result = false;
        }
        return result;
    }
    
    protected static byte[][] deriveKeyAndIV(byte[] salt, byte[] master_key) throws NoSuchAlgorithmException, IOException {
        
        byte[] key = new byte[KEY_LEN];
        byte[] IV = new byte[IV_LEN];
        
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ByteArrayOutputStream toHash = new ByteArrayOutputStream();
        byte[] d = null;
        
        while (data.size() < IV_LEN + KEY_LEN) {
            md5.reset();
            toHash.reset();
            if (d != null) 
                toHash.write(d);
            toHash.write(master_key);
            toHash.write(salt);
            d = md5.digest(toHash.toByteArray());
            data.write(d);
        }
        
        byte[] output = data.toByteArray();
        
        System.arraycopy(output, 0, key, 0, KEY_LEN);
        System.arraycopy(output, KEY_LEN, IV, 0, IV_LEN);

        return new byte[][]{key, IV};
    }
    
    public static String encrypt(byte[] key, String message) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[8];
        sr.nextBytes(salt);
                
        byte[][] key_and_iv = deriveKeyAndIV(salt, key);
        
        SecretKeySpec enc_key;
        enc_key = new SecretKeySpec(key_and_iv[0], "AES");
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(key_and_iv[1]);
        aes.init(Cipher.ENCRYPT_MODE, enc_key, iv);
        
        byte[] salted = "Salted__".getBytes("UTF-8");
        byte[] cipher = aes.doFinal(message.getBytes("UTF-8"));
        
        byte[] result = new byte[salted.length + salt.length + cipher.length];

        // now we need to glue: "Salted__" + salt + cipher
        System.arraycopy(salted, 0, result, 0, salted.length);
        System.arraycopy(salt, 0, result, salted.length, salt.length);
        System.arraycopy(cipher, 0, result, salted.length + salt.length, cipher.length);
        
        // remove = and FWKNOP_ENCRYPTION_HEADER
        return Base64.encodeBase64String(result).replace("=", "").replace(FWKNOP_ENCRYPTION_HEADER, "");
    }
    
    public static String decrypt(byte[] key, String ciphertext) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (!ciphertext.startsWith(FWKNOP_ENCRYPTION_HEADER)) {
            ciphertext = FWKNOP_ENCRYPTION_HEADER.concat(ciphertext);
        }
        // we need to remove Salted__ from the salt_and_ciphertext therefore -> SALT_LEN +/- 8
        
        byte[] salt_and_ciphertext = Base64.decodeBase64(ciphertext);              
        byte[] salt = new byte[SALT_LEN];        
        byte[] cipher = new byte[salt_and_ciphertext.length - SALT_LEN - 8];
        System.arraycopy(salt_and_ciphertext, 8, salt, 0, SALT_LEN);
        System.arraycopy(salt_and_ciphertext, 8 + SALT_LEN, cipher, 0, cipher.length);
        byte[][] key_and_iv = deriveKeyAndIV(salt, key);
        
        
        SecretKeySpec enc_key;
        enc_key = new SecretKeySpec(key_and_iv[0], "AES");
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(key_and_iv[1]);
        aes.init(Cipher.DECRYPT_MODE, enc_key, iv);
        byte[] plain = aes.doFinal(cipher);
        
        return new String(plain, "UTF-8");
    }
    
}
