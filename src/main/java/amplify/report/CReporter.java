package amplify.report;

import amplify.*;
import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

@Deprecated
public class CReporter implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    private final Pointer ptr;

    CReporter(final Pointer ptr) { this.ptr = ptr; }

    public CReporter setSendTimeout(final Timeout timeout) {
        LIB.creporter_set_tx_timeout(this.ptr, timeout.millis);
        return this;
    }

    public CReporter setSendHwm(final Hwm hwm) {
        LIB.creporter_set_tx_hwm(this.ptr, hwm.capacity);
        return this;
    }

    public void send(final Report report) {  LIB.creporter_send(this.ptr, report.ptr);  }

    public void destroy() {  LIB.creporter_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

}
