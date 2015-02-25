package net.seleucus.wsp.crypto.fwknop.fields;

public enum DigestType {

    MD5(1, "MD5"),
    SHA1(2, "SHA-1"),
    SHA256(3, "SHA-256"),
    SHA384(4, "SHA-384"),
    SHA512(5, "SHA-512");

    private final int id;
    private final String algorithmName;

    private DigestType(final int id, final String algorithmName) {
        this.id = id;
        this.algorithmName = algorithmName;
    }

    public int getId() {
        return id;
    }

    public String algorithmName() {
        return algorithmName;
    }
}
