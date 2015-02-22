package net.seleucus.wsp.crypto.fwknop.fields;

public enum DigestType {

    MD5(1),
    SHA1(2),
    SHA256(3),
    SHA384(4),
    SHA512(5);

    private final int id;

    private DigestType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
