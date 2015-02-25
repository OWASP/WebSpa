package net.seleucus.wsp.crypto.fwknop.fields;

public enum EncryptionType {

    AES(1, "AES", 16);

    private final int id;
    private final String name;
    private final int blockSize;

    private EncryptionType(final int id, final String name, int blockSize) {
        this.id = id;
        this.name = name;
        this.blockSize = blockSize;
    }

    public int getId() {
        return id;
    }

    public String algorithmName() {
        return name;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
