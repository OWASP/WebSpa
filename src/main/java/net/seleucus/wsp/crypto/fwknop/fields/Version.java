package net.seleucus.wsp.crypto.fwknop.fields;

public enum Version {

    CURRENT("2.0.1");

    private final String id;

    private Version(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
