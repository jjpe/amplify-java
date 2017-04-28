package amplifier;

import core.Component;
import core.cerealize.CapnpCerealizer;
import core.cerealize.ICerealizer;
import org.zeromq.ZMQ;

public class Client {
    private final String name;
    private final Component rx;
    private final Component tx;
    private String rxAddress;
    private String txAddress;
    private ICerealizer cerealizer;

    public Client(final String name, final ICerealizer cerealizer) {
        this.name = name;
        this.rx = new Component(ZMQ.SUB);
        this.tx = new Component(ZMQ.PUSH);
        this.rxAddress = "tcp://127.0.0.1:6001";
        this.txAddress = "tcp://127.0.0.1:6000";
        this.cerealizer = cerealizer;
    }

    public Client(final String name) {  this(name, new CapnpCerealizer());  }

    public Client receiveAddress(final String addr) {
        this.rxAddress = addr;
        return this;
    }

    public Client sendAddress(final String addr) {
        this.txAddress = addr;
        return this;
    }

    public Client connect() {
        this.rx.connect(this.rxAddress);
        this.tx.connect(this.txAddress);
        return this;
    }

    public void send(final Msg msg, final int flags) {
        final byte[] bytes = this.cerealizer.cerealize(msg, Msg.class);
        this.tx.sendBytes(bytes, flags);
    }

    public Msg receive(final int flags) {
        final byte[] bytes = this.rx.receiveBytes(flags);
        return this.cerealizer.decerealize(bytes, Msg.class);
    }
}
