package net.seleucus.wsp.crypto.fwknop.fields;

public enum EncryptionType {

    AES(1),
    GPG(2);

    private final int id;

    private EncryptionType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
