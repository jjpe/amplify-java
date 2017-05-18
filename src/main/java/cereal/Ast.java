package cereal;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

public class Ast implements Closeable {
    private static final LibCereal LIB = LibCereal.INSTANCE;

    final Pointer ptr;

    Ast(final Pointer ptr) {  this.ptr = ptr;  }

    public Ast(final String name) {  this.ptr = LIB.ast_new(name);  }

    public void destroy() {  LIB.ast_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }


    public String getData() {  return LIB.ast_get_data(this.ptr);  }

    public void setData(final String data) {  LIB.ast_set_data(this.ptr, data);  }

    public void clearData() {  LIB.ast_clear_data(this.ptr);  }

    public void addChild(final Ast child) {  LIB.ast_add_child(this.ptr, child.ptr);  }

    public Ast getChild(final long index) {  return new Ast(LIB.ast_get_child(this.ptr, index));  }

    public void clearChildren() {  LIB.ast_clear_children(this.ptr);  }

    public void countChildren() {  LIB.ast_count_children(this.ptr);  }
}
