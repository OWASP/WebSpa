package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import net.seleucus.wsp.crypto.fwknop.fields.MessageType;
import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.util.Objects.requireNonNull;
import static net.seleucus.wsp.crypto.fwknop.FwknopBase64.encode;
import static org.apache.commons.codec.Charsets.UTF_8;

public class Message {

    public static final char FIELD_DELIMITER = ':';

    private final long randomValue;
    private final String username;
    private final long timestamp;
    private final String version;
    private final MessageType messageType;
    private final String payload;
    private final DigestType digestType;

    Message(long randomValue, String username, long timestamp, String version, MessageType messageType, String payload, DigestType digestType) {
        this.digestType = digestType;
        this.randomValue = requireNonNull(randomValue);
        this.username = requireNonNull(username);
        this.timestamp = requireNonNull(timestamp);
        this.version = requireNonNull(version);
        this.messageType = requireNonNull(messageType);
        this.payload = requireNonNull(payload);
    }

    public String encoded(){
        return new StringBuilder()
            .append(randomValue).append(FIELD_DELIMITER)
            .append(encode(username)).append(FIELD_DELIMITER)
            .append(timestamp).append(FIELD_DELIMITER)
            .append(version).append(FIELD_DELIMITER)
            .append(messageType.getId()).append(FIELD_DELIMITER)
            .append(encode(payload))
            .toString();
    }

    public String encodedWithDigest() throws NoSuchAlgorithmException {
        return encoded() + FIELD_DELIMITER + base64digest();
    }

    public byte[] digest() throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(digestType.algorithmName());
        return digest.digest(encoded().getBytes(UTF_8));
    }

    public String base64digest() throws NoSuchAlgorithmException {
        return encode(digest());
    }

    public long randomValue() {
        return randomValue;
    }

    public String username() {
        return username;
    }

    public String encodedUserName(){
        return Base64.encodeBase64String(username.getBytes());
    }

    public long timestamp() {
        return timestamp;
    }

    public MessageType messageType() {
        return messageType;
    }

    public String version() {
        return version;
    }

    public String payload() {
        return payload;
    }
}
