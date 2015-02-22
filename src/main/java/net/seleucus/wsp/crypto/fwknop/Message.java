package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.*;

public class Message {

    private final long randomValue; // 16 byte number
    private final String username; // base64 encoded
    private final long timestamp; // Unix timestamp
    private final Version version;
    private final MessageType messageType; //
    private final EncryptionType encryptionType;
    private final EncryptionMode encryptionMode;
    private final String message;

    Message(long randomValue, String username, long timestamp, Version version, MessageType messageType, EncryptionType encryptionType, EncryptionMode encryptionMode, String message) {
        this.randomValue = randomValue;
        this.username = username;
        this.timestamp = timestamp;
        this.version = version;
        this.messageType = messageType;
        this.encryptionType = encryptionType;
        this.encryptionMode = encryptionMode;
        this.message = message;
    }

    public long getRandomValue() {
        return randomValue;
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Version getVersion() {
        return version;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    public String getMessage() {
        return message;
    }
}
