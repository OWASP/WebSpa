package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import net.seleucus.wsp.crypto.fwknop.fields.MessageType;

import static net.seleucus.wsp.crypto.fwknop.fields.DigestType.SHA256;
import static net.seleucus.wsp.crypto.fwknop.fields.MessageType.AccessMessage;

public class MessageBuilder {

    private long randomValue;
    private String username;
    private long timestamp;
    private String version = "2.0.2";
    private MessageType messageType = AccessMessage;
    private String payload;
    private DigestType digestType = SHA256;

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

    public MessageBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public MessageBuilder withMessageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageBuilder withPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public MessageBuilder withDigestType(DigestType digestType) {
        this.digestType = digestType;
        return this;
    }

    public Message build(){
        return new Message(randomValue, username, timestamp, version, messageType, payload, digestType);
    }
}
