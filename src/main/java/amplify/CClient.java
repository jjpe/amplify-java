package amplify;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

/**
 * Proxy convenience class to manipulate connected libamplify clients.
 */
public class CClient implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    private final Pointer ptr;

    CClient(final Pointer ptr) { this.ptr = ptr; }

    public CClient setSendTimeout(final Timeout timeout) {
        LIB.cclient_set_tx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public CClient setReceiveTimeout(final Timeout timeout) {
        LIB.cclient_set_rx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public CClient setSendHwm(final Hwm hwm) {
        LIB.cclient_set_tx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public CClient setReceiveHwm(final Hwm hwm) {
        LIB.cclient_set_rx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public void send(final Msg msg) {  LIB.cclient_send(this.ptr, msg.ptr);  }

    public void receive(final Msg msg) {  LIB.cclient_receive(this.ptr, msg.ptr);  }

    public void destroy() {  LIB.cclient_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

}
