package net.seleucus.wsp.crypto.fwknop.fields;

public enum EncryptionMode {

    ECB(1, "ECB"),
    CBC(2, "CBC"),
    CFB(3, "CFB"),
    PCBC(4, "PCBC"),
    OFB(5, "OFB"),
    CTR(6, "CTR");

    private final int id;
    private final String name;

    private EncryptionMode(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int id() {
        return id;
    }

    public String modeName() {
        return name;
    }
}
