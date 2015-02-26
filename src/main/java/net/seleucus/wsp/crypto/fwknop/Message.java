package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.MessageType;
import net.seleucus.wsp.crypto.fwknop.fields.Version;
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
    private final String payload;

    Message(long randomValue, String username, long timestamp, Version version, MessageType messageType, String payload) {
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

    public String payload() {
        return payload;
    }
}
