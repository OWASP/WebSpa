package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.*;
import org.apache.commons.codec.binary.Base64;

import static java.util.Objects.requireNonNull;
import static net.seleucus.wsp.crypto.fwknop.FwknopBase64.encode;

public class Message {

    public static final char FIELD_DELIMITER = ':';

    private final long randomValue;
    private final String username;
    private final long timestamp;
    private final Version version;
    private final MessageType messageType;
    private final EncryptionType encryptionType;
    private final EncryptionMode encryptionMode;
    private final DigestType digestType;
    private final String payload;

    Message(long randomValue, String username, long timestamp, Version version, MessageType messageType, EncryptionType encryptionType, EncryptionMode encryptionMode, DigestType digestType, String payload) {
        this.randomValue = requireNonNull(randomValue);
        this.username = requireNonNull(username);
        this.timestamp = requireNonNull(timestamp);
        this.version = requireNonNull(version);
        this.messageType = requireNonNull(messageType);
        this.encryptionType = requireNonNull(encryptionType);
        this.encryptionMode = requireNonNull(encryptionMode);
        this.digestType = requireNonNull(digestType);
        this.payload = requireNonNull(payload);
    }

    public String encoded(){
        return new StringBuilder()
            .append(randomValue).append(FIELD_DELIMITER)
            .append(encode(username)).append(FIELD_DELIMITER)
            .append(timestamp).append(FIELD_DELIMITER)
            .append(version.getId()).append(FIELD_DELIMITER)
            .append(messageType.getId()).append(FIELD_DELIMITER)
            .append(encode(payload))
            .toString();
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

    public Version version() {
        return version;
    }

    public EncryptionType encryptionType() {
        return encryptionType;
    }

    public EncryptionMode encryptionMode() {
        return encryptionMode;
    }

    public DigestType digestType() {
        return digestType;
    }

    public String payload() {
        return payload;
    }
}
