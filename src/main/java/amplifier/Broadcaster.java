package amplifier;

import core.Component;
import core.cerealize.CapnpCerealizer;
import core.cerealize.ICerealizer;
import org.zeromq.ZMQ;

public class Broadcaster {
    private static final String NAME = "broadcaster";
    private final Component rx;
    private final Component tx;
    private String rxAddress;
    private String txAddress;
    private ICerealizer cerealizer;

    public Broadcaster(final ICerealizer cerealizer) {
        this.rx = new Component(ZMQ.PULL);
        this.tx = new Component(ZMQ.PUB);
        this.rxAddress = "tcp://127.0.0.1:6000";
        this.txAddress = "tcp://127.0.0.1:6001";
        this.cerealizer = cerealizer;
    }

    public Broadcaster() {  this(new CapnpCerealizer());  }

    public Broadcaster receiveAddress(final String addr) {
        this.rxAddress = addr;
        return this;
    }

    public Broadcaster broadcastAddress(final String addr) {
        this.txAddress = addr;
        return this;
    }

    public Broadcaster bind() {
        this.rx.bind(this.rxAddress);
        this.tx.bind(this.txAddress);
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
