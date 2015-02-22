package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.EncryptionMode;
import net.seleucus.wsp.crypto.fwknop.fields.EncryptionType;
import net.seleucus.wsp.crypto.fwknop.fields.MessageType;
import net.seleucus.wsp.crypto.fwknop.fields.Version;

import static net.seleucus.wsp.crypto.fwknop.fields.EncryptionMode.CBC;
import static net.seleucus.wsp.crypto.fwknop.fields.EncryptionType.AES;
import static net.seleucus.wsp.crypto.fwknop.fields.MessageType.AccessMessage;
import static net.seleucus.wsp.crypto.fwknop.fields.Version.CURRENT;

public class MessageBuilder {

    private long randomValue;
    private String username;
    private long timestamp;
    private Version version = CURRENT;
    private MessageType messageType = AccessMessage;
    private EncryptionType encryptionType = AES;
    private EncryptionMode encryptionMode = CBC;
    private String message;

    public static MessageBuilder createMessage(){
        return new MessageBuilder();
    }

    public MessageBuilder withRandomValue(long randomValue) {
        this.randomValue = randomValue;
        return this;
    }

    public MessageBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public MessageBuilder withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public MessageBuilder withVersion(Version version) {
        this.version = version;
        return this;
    }

    public MessageBuilder withMessageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageBuilder withEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public MessageBuilder withEncryptionMode(EncryptionMode encryptionMode) {
        this.encryptionMode = encryptionMode;
        return this;
    }

    public MessageBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public Message build(){
        return new Message(randomValue, username, timestamp, version, messageType, encryptionType, encryptionMode, message);
    }
}
