package net.seleucus.wsp.crypto.fwknop.fields;

public enum HmacType {

    HmacMD5(0),
    HmacSHA1(1),
    HmacSHA256(2),
    HmacSHA384(3),
    HmacSHA512(4);

    private final int id;

    private HmacType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
