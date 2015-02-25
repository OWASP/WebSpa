/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seleucus.wsp.crypto;

import net.seleucus.wsp.crypto.fwknop.FwknopBase64;
import net.seleucus.wsp.crypto.fwknop.Message;
import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static net.seleucus.wsp.crypto.fwknop.Message.FIELD_DELIMITER;

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

    private static final String PADDING_STRATEGY = "NoPadding";
    private final static String FWKNOP_ENCRYPTION_HEADER = "U2FsdGVkX1";
    private final static byte SALT_LEN = 8;
    private final static byte IV_LEN = 16;
    private final static byte KEY_LEN = 32;
    
    protected final static byte[] DIGEST_BASE64_LENGTH = {22, 27, 43, 64, 86};
    private final static String[] HMAC_ALGORITHMS = {"HmacMD5", "HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512"};

    private static final SecureRandom secureRandom = new SecureRandom();

    static class MessageKey {
        private final byte[] key;
        private final byte[] initialisationVector;

        private MessageKey(byte[] key, byte[] initalisationVector) {
            this.key = key;
            this.initialisationVector = initalisationVector;
        }

        public byte[] getKey() {
            return key;
        }

        public byte[] getInitialisationVector() {
            return initialisationVector;
        }
    }
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
    
    protected static MessageKey deriveKeyAndIV(byte[] salt, byte[] master_key) throws NoSuchAlgorithmException, IOException {
        
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

        return new MessageKey(key, IV);
    }

    public static String encrypt(final byte[] masterKey, Message message) throws Exception {
        final byte[] salt = new byte[SALT_LEN];
        secureRandom.nextBytes(salt);
        return encrypt(masterKey, salt, message);
    }

    public static String encrypt(final byte[] masterKey, final byte[] salt, Message message) throws Exception {

        final MessageKey messageKey = deriveKeyAndIV(salt, masterKey);

        final SecretKeySpec secretKey = new SecretKeySpec(messageKey.key, message.encryptionType().algorithmName());
        final Cipher cipher = getCipher(message);
        final IvParameterSpec initialisationVector = new IvParameterSpec(messageKey.initialisationVector);
        cipher.init(ENCRYPT_MODE, secretKey, initialisationVector);

        final String plaintextMessage = getPaddedMessage(message);

        final byte[] prefix = "Salted__".getBytes("UTF-8");
        final byte[] cipherText = cipher.doFinal(plaintextMessage.toString().getBytes(Charsets.UTF_8));

        final byte[] encryptedMessage = ByteBuffer.allocate(prefix.length + salt.length + cipherText.length)
                .put(prefix)
                .put(salt)
                .put(cipherText)
                .array();

        return Base64.encodeBase64String(encryptedMessage).replace("=", "").replace(FWKNOP_ENCRYPTION_HEADER, "");
    }

    private static String getPaddedMessage(final Message message) throws NoSuchAlgorithmException {
        final String encodedMessage = message.encoded();

        if(encodedMessage.length() % message.encryptionType().getBlockSize() == 0){
            return encodedMessage;
        }

        final String digest = getBase64Digest(message.digestType(), message.encoded());
        final int wholeBlocks = encodedMessage.length() / message.encryptionType().getBlockSize();
        final int paddingChars = ((wholeBlocks + 1) * message.encryptionType().getBlockSize()) - encodedMessage.length() - 1;

        return encodedMessage +  FIELD_DELIMITER + StringUtils.substring(digest, 0, paddingChars);

    }

    private static String getBase64Digest(final DigestType digestType, final String input) throws NoSuchAlgorithmException {
        return Base64.encodeBase64String(getDigest(digestType, input));
    }

    private static byte[] getDigest(final DigestType digestType, final String input) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(digestType.algorithmName());
        return digest.digest(input.getBytes(Charsets.UTF_8));
    }

    public static Cipher getCipher(Message message) throws NoSuchPaddingException, NoSuchAlgorithmException {
        final String algorithmName = new StringBuilder()
                .append(message.encryptionType().algorithmName())
                .append("/")
                .append(message.encryptionMode().modeName())
                .append("/")
                .append(PADDING_STRATEGY)
                .toString();

        return Cipher.getInstance(algorithmName);
    }
    
    public static String decrypt(byte[] masterKey, String ciphertext) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (!ciphertext.startsWith(FWKNOP_ENCRYPTION_HEADER)) {
            ciphertext = FWKNOP_ENCRYPTION_HEADER.concat(ciphertext);
        }

        // we need to remove Salted__ from the salt_and_ciphertext therefore -> SALT_LEN +/- 8
        final byte[] cipherTextBytes = FwknopBase64.decode(ciphertext);
        final byte[] salt = new byte[SALT_LEN];
        final byte[] cipherText = new byte[cipherTextBytes.length - SALT_LEN - 8];
        System.arraycopy(cipherTextBytes, 8, salt, 0, SALT_LEN);
        System.arraycopy(cipherTextBytes, 8 + SALT_LEN, cipherText, 0, cipherText.length);

        final MessageKey messageKey = deriveKeyAndIV(salt, masterKey);

        final SecretKeySpec secretKey = new SecretKeySpec(messageKey.key, "AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        final IvParameterSpec initialisationVector = new IvParameterSpec(messageKey.initialisationVector);
        cipher.init(DECRYPT_MODE, secretKey, initialisationVector);

        final byte[] plainText = cipher.doFinal(cipherText);
        
        return new String(plainText, "UTF-8");
    }
}
