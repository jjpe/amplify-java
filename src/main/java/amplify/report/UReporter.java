package amplify.report;

import amplify.Hwm;
import amplify.LibAmplify;
import amplify.Timeout;
import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

public class UReporter implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    private final Pointer ptr;

    public UReporter() {  this.ptr = LIB.ureporter_new();  }

    public UReporter setSendAddr(final String addr) {
        LIB.ureporter_set_tx_addr(this.ptr, addr);
        return this;
    }

    public UReporter serializeUsingJson() {
        LIB.ureporter_serialize_using_json(this.ptr);
        return this;
    }

    public UReporter serializeUsingCapnProto() {
        LIB.ureporter_serialize_using_capn_proto(this.ptr);
        return this;
    }

    public UReporter setSendTimeout(final Timeout timeout) {
        LIB.ureporter_set_tx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public UReporter setSendHwm(final Hwm hwm) {
        LIB.ureporter_set_tx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public CReporter connect() {
        return new CReporter(LIB.ureporter_connect(this.ptr));
    }


    public void destroy() {  LIB.ureporter_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }
}
