package net.seleucus.wsp.crypto.fwknop.fields;

public enum MessageType {

    CommandMessage(0),
    AccessMessage(1),
    NatAccessMessage(2),
    ClientTimeOutAccessMessage(3),
    ClientTimeOutNatAccessMessage(4),
    LocalNatAccessMessage(5),
    ClientTimeoutLocalNatAccessMessage(6);

    private int id;

    private MessageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
