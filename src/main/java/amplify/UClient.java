package amplify;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

/**
 * Proxy convenience class to manipulate unconnected libamplify clients.
 */
public class UClient implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    private final Pointer ptr;

    public UClient() {  this.ptr = LIB.uclient_new();  }

    public UClient setReceiveAddr(final String addr) {
        LIB.uclient_set_rx_addr(this.ptr, addr);
        return this;
    }

    public UClient setSendAddr(final String addr) {
        LIB.uclient_set_tx_addr(this.ptr, addr);
        return this;
    }

    public UClient serializeUsingJson() {
        LIB.uclient_serialize_using_json(this.ptr);
        return this;
    }

    public UClient serializeUsingCapnProto() {
        LIB.uclient_serialize_using_capn_proto(this.ptr);
        return this;
    }

    public UClient setSendTimeout(final Timeout timeout) {
        LIB.uclient_set_tx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public UClient setReceiveTimeout(final Timeout timeout) {
        LIB.uclient_set_rx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public UClient setSendHwm(final Hwm hwm) {
        LIB.uclient_set_tx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public UClient setReceiveHwm(final Hwm hwm) {
        LIB.uclient_set_rx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public CClient connect() {
        return new CClient(LIB.uclient_connect(this.ptr));
    }


    public void destroy() {  LIB.uclient_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

}
