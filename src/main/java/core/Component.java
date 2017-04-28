package core;

import org.zeromq.ZContext;
import org.zeromq.ZSocket;

public class Component {
    private final ZContext context;
    private final ZSocket socket;

    public Component(final int socketType) {
        this(new ZContext(), socketType);
    }

    public Component(final ZContext context,
                     final int socketType) {
        this.context = context;
        this.socket = new ZSocket(socketType);
    }

    public void connect(final String address) {
        this.socket.connect(address);
    }

    public void bind(final String address) {
        this.socket.bind(address);
    }

    public void sendBytes(final byte[] bytes, final int flags) {
        if (this.socket.send(bytes, flags) == -1) {
            // TODO: handle send error
        }
    }

    public void sendUtf8String(final String utf8, final int flags) {
        if (this.socket.sendStringUtf8(utf8, flags) == -1) {
            // TODO: handle send error
        }
    }

    public void sendAck(final int flags) {
        this.sendUtf8String("ACK", flags);
    }

    public byte[] receiveBytes(final int flags) {
        return this.socket.receive(flags);
    }

    public String receiveUtf8String(final int flags) {
        return this.socket.receiveStringUtf8(flags);
    }

    public void receiveAck(final int flags) {
        final String received = this.receiveUtf8String(flags);
        if (!"ACK".equals(received)) {
            // TODO: Handle error: got something but it's not ACK
        }
    }
}
