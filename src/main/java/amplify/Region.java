package amplify;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

public class Region implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    final Pointer ptr;

    Region(final Pointer ptr) {  this.ptr = ptr;  }

    public Region(final long begin, final long end) {
        this.ptr = LIB.region_new(begin, end);
    }

    public long getBegin() {  return LIB.region_get_begin(this.ptr);  }

    public long getEnd() {  return LIB.region_get_end(this.ptr);  }

    public void destroy() {  LIB.region_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

    @Override
    public String toString() {  return String.format("[%d, %d)", this.getBegin(), this.getEnd());  }
}
