package net.seleucus.wsp.crypto.fwknop;

import static java.util.Arrays.copyOf;

public class MessageKey {

    private final byte[] key;
    private final byte[] initialisationVector;

    MessageKey(byte[] key, byte[] initialisationVector) {
        this.key = copyOf(key, key.length);
        this.initialisationVector = copyOf(initialisationVector, initialisationVector.length);
    }

    public byte[] key() {
        return key;
    }

    public byte[] initialisationVector() {
        return initialisationVector;
    }
}
