package net.seleucus.wsp.crypto.fwknop.fields;

public enum EncryptionMode {

    ECB(1),
    CBC(2),
    CFB(3),
    PCBC(4),
    OFB(5),
    CTR(6);

    private final int id;

    private EncryptionMode(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
