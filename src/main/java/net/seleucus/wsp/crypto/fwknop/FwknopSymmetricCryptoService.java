package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.WebSpaUtils;
import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import net.seleucus.wsp.crypto.fwknop.fields.EncryptionMode;
import net.seleucus.wsp.crypto.fwknop.fields.EncryptionType;
import net.seleucus.wsp.crypto.fwknop.fields.HmacType;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static net.seleucus.wsp.crypto.fwknop.Message.FIELD_DELIMITER;

/**
 *
 * @author pgolen
 */
public final class FwknopSymmetricCryptoService extends WebSpaUtils {

    private static final String PADDING_STRATEGY = "NoPadding";
    private final static String FWKNOP_ENCRYPTION_HEADER = "U2FsdGVkX1";

    private final static byte SALT_LEN = 8;
    private final static byte IV_LEN = 16;
    private final static byte KEY_LEN = 32;

    private final SecureRandom secureRandom;
    private final EncryptionType encryptionType;
    private final EncryptionMode encryptionMode;
    private final DigestType digestType;

    public FwknopSymmetricCryptoService(SecureRandom secureRandom, EncryptionType encryptionType, EncryptionMode encryptionMode, DigestType digestType) {
        this.secureRandom = secureRandom;
        this.encryptionType = encryptionType;
        this.encryptionMode = encryptionMode;
        this.digestType = digestType;
    }

    public String sign(byte[] keyBytes, String message, HmacType hmacType) throws Exception {

        final Mac hmac = Mac.getInstance(hmacType.algorithmName());
        final SecretKeySpec hmacKey = new SecretKeySpec(keyBytes, hmacType.algorithmName());
        hmac.init(hmacKey);
        
        // Prepare enc_part to calculate HMAC
        final byte[] msg_to_hmac = FWKNOP_ENCRYPTION_HEADER.concat(message).getBytes();
        
        // Calculate HMAC and return
        return message.concat(Base64.encodeBase64String(hmac.doFinal(msg_to_hmac)).replace("=", ""));
    }        
    
    public boolean verify(byte[] auth_key, String message, HmacType hmacType) throws Exception {
        // Split message into two parts, encrypted payload and HMAC
        String enc_part = message.substring(0, message.length() - hmacType.base64Length());
        String auth_part = message.substring(message.length() - hmacType.base64Length(), message.length());
       
        // Calculate HMAC, compare, return results
        return equals(sign(auth_key, enc_part, hmacType), message);
    }
    
    protected boolean equals(CharSequence a, CharSequence b) {
        if (a.length() != b.length())
            return false;
        boolean result = true;
        for (int i=0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                result = false;
        }
        return result;
    }
    
    protected MessageKey deriveKeyAndIV(byte[] salt, byte[] master_key) throws Exception {
        
        final byte[] key = new byte[KEY_LEN];
        final byte[] IV = new byte[IV_LEN];
        
        final MessageDigest md5 = MessageDigest.getInstance("MD5");

        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        final ByteArrayOutputStream toHash = new ByteArrayOutputStream();

        byte[] d = null;
        
        while (data.size() < IV_LEN + KEY_LEN) {
            md5.reset();
            toHash.reset();
            if (d != null){
                toHash.write(d);
            }
            toHash.write(master_key);
            toHash.write(salt);
            d = md5.digest(toHash.toByteArray());
            data.write(d);
        }
        
        final byte[] output = data.toByteArray();
        
        System.arraycopy(output, 0, key, 0, KEY_LEN);
        System.arraycopy(output, KEY_LEN, IV, 0, IV_LEN);

        return new MessageKey(key, IV);
    }

    public String encrypt(final byte[] masterKey, Message message) throws Exception {
        final byte[] salt = new byte[SALT_LEN];
        secureRandom.nextBytes(salt);
        return encrypt(masterKey, salt, message);
    }

    public String encrypt(final byte[] masterKey, final byte[] salt, Message message) throws Exception {

        final MessageKey messageKey = deriveKeyAndIV(salt, masterKey);

        final SecretKeySpec secretKey = new SecretKeySpec(messageKey.key(), encryptionType.algorithmName());
        final Cipher cipher = getCipher();
        final IvParameterSpec initialisationVector = new IvParameterSpec(messageKey.initialisationVector());
        cipher.init(ENCRYPT_MODE, secretKey, initialisationVector);

        final String plaintextMessage = getPaddedMessage(message);

        final byte[] prefix = "Salted__".getBytes("UTF-8");
        final byte[] cipherText = cipher.doFinal(plaintextMessage.getBytes(Charsets.UTF_8));

        final byte[] encryptedMessage = ByteBuffer.allocate(prefix.length + salt.length + cipherText.length)
                .put(prefix)
                .put(salt)
                .put(cipherText)
                .array();

        return Base64.encodeBase64String(encryptedMessage).replace("=", "").replace(FWKNOP_ENCRYPTION_HEADER, "");
    }

    private String getPaddedMessage(final Message message) throws NoSuchAlgorithmException {
        final String encodedMessage = message.encoded();

        if(encodedMessage.length() % encryptionType.getBlockSize() == 0){
            return encodedMessage;
        }

        final String digest = getBase64Digest(message.encoded());
        final int wholeBlocks = encodedMessage.length() / encryptionType.getBlockSize();
        final int paddingChars = ((wholeBlocks + 1) * encryptionType.getBlockSize()) - encodedMessage.length() - 1;

        return encodedMessage +  FIELD_DELIMITER + StringUtils.substring(digest, 0, paddingChars);
    }

    private String getBase64Digest(final String input) throws NoSuchAlgorithmException {
        return Base64.encodeBase64String(getDigest(digestType, input));
    }

    private byte[] getDigest(final DigestType digestType, final String input) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(digestType.algorithmName());
        return digest.digest(input.getBytes(Charsets.UTF_8));
    }

    public Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        final String algorithmName = new StringBuilder()
                .append(encryptionType.algorithmName())
                .append("/")
                .append(encryptionMode.modeName())
                .append("/")
                .append(PADDING_STRATEGY)
                .toString();

        return Cipher.getInstance(algorithmName);
    }
    
    public String decrypt(byte[] masterKey, String ciphertext) throws Exception {
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

        final SecretKeySpec secretKey = new SecretKeySpec(messageKey.key(), "AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        final IvParameterSpec initialisationVector = new IvParameterSpec(messageKey.initialisationVector());
        cipher.init(DECRYPT_MODE, secretKey, initialisationVector);

        final byte[] plainText = cipher.doFinal(cipherText);
        
        return new String(plainText, "UTF-8");
    }
}
