package net.seleucus.wsp.crypto.fwknop.fields;

public enum HmacType {

    HmacMD5(0, "HmacMD5", 16, 22),
    HmacSHA1(1, "HmacSHA1", 20, 27),
    HmacSHA256(2, "HmacSHA256", 32, 43),
    HmacSHA384(3, "HmacSHA384", 48, 64),
    HmacSHA512(4, "HmacSHA512", 64, 86);

    private final int id;
    private final String algorithmName;
    private final int length;
    private final int base64Length;

    private HmacType(int id, String algorithmName, int length, int base64Length) {
        this.id = id;
        this.algorithmName = algorithmName;
        this.length = length;
        this.base64Length = base64Length;
    }

    public int id() {
        return id;
    }

    public String algorithmName() {
        return algorithmName;
    }

    public int length() {
        return length;
    }

    public int base64Length(){
        return base64Length;
    }
}
