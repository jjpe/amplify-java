package cereal;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

/**
 * Proxy convenience class to manipulate connected libamplify clients.
 */
public class CClient implements Closeable {
    private static final LibCereal LIB = LibCereal.INSTANCE;

    private final Pointer ptr;

    CClient(final Pointer ptr) { this.ptr = ptr; }

    public void send(final Msg msg) {  LIB.cclient_send(this.ptr, msg.ptr);  }

    public void receive(final Msg msg) {  LIB.cclient_receive(this.ptr, msg.ptr);  }

    public void destroy() {  LIB.cclient_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

}
