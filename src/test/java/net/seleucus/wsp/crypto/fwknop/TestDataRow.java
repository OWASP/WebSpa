package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import net.seleucus.wsp.crypto.fwknop.fields.HmacType;
import net.seleucus.wsp.crypto.fwknop.fields.MessageType;

public class TestDataRow {

    private byte[] encryptionKey;
    private byte[] signatureKey;
    private DigestType digestType;
    private HmacType hmacType;
    private long randomValue;
    private String userName;
    private long timestamp;
    private String version;
    private MessageType messageType;
    private String message;
    private String digest;
    private String encodedData;
    private String spaData;
    private String hmac;

    public byte[] encryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public byte[] signatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(byte[] signatureKey) {
        this.signatureKey = signatureKey;
    }

    public DigestType digestType() {
        return digestType;
    }

    public void setDigestType(DigestType digestType) {
        this.digestType = digestType;
    }

    public HmacType hmacType() {
        return hmacType;
    }

    public void setHmacType(HmacType hmacType) {
        this.hmacType = hmacType;
    }

    public long randomValue() {
        return randomValue;
    }

    public void setRandomValue(long randomValue) {
        this.randomValue = randomValue;
    }

    public String userName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long timestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String version() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MessageType messageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String digest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
    }

    public String getSpaData() {
        return spaData;
    }

    public void setSpaData(String spaData) {
        this.spaData = spaData;
    }

    public String hHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}
