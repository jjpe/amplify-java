package amplifier;

import cereal.CClient;
import cereal.LibCereal;
import cereal.UClient;
import com.sun.jna.Pointer;
import core.Component;
import core.cerealize.ICerealizer;
import org.zeromq.ZMQ;

import java.util.Properties;

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
        this.tx = new Component(ZMQ.REQ);
        this.rxAddress = "tcp://127.0.0.1:6001";
        this.txAddress = "tcp://127.0.0.1:6000";
        this.cerealizer = cerealizer;
    }

    public Client(final String name) {  this(name, new Msg.CapnpCerealizer());  }

    public String getName() { return this.name; }

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
        final byte[] bytes = this.cerealizer.cerealize(msg);
        this.tx.sendBytes(bytes, flags);
        if (!this.tx.receiveAck(0)) {
            System.out.println("UClient.send: Error receiving ACK");
            // TODO: error
        }
    }

    public Msg receive(final int flags) {
        final byte[] bytes = this.rx.receiveBytes(flags);
        return this.cerealizer.decerealize(bytes);
    }


//    public static void main(final String[] args) {
//        final UClient client = new UClient("test client")
//                .connect();

//        final List<String> entries = new ArrayList<>();
//        entries.add("foo bar baz!");
//        entries.add("qux quux corge grault.");
//
//        final List<Region> regions = new ArrayList<>();
//        regions.add(new Region(0, 1));
//        regions.add(new Region(10, 11));
//
//        final Ast ast = new Ast("Sum");
//        ast.addChildren(
//                new Ast("Int").addData("2"),
//                new Ast("Int").addData("3")
//        );
//
//        final Msg msg = new Msg("Msg Source")
//                .setRequestNumber(9001)
//                .setOrigin("client tester (java) origin")
////                .setContents(new Contents("flah"))
////                .setContents(null)
//                .setContents(new Contents(entries)) // TODO: this fails ATM
//                .setRegions(regions)
//                .setLanguage(new Language("JibberJabber"))
//                .setAst(ast);
//        client.send(msg, 0);
//    }
}
