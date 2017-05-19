package cereal;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ast implements Closeable {
    private static final LibCereal LIB = LibCereal.INSTANCE;

    final Pointer ptr;

    Ast(final Pointer ptr) {  this.ptr = ptr;  }

    public Ast(final String name) {  this.ptr = LIB.ast_new(name);  }

    public void destroy() {  LIB.ast_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }


    public String getName() {  return LIB.ast_get_name(this.ptr);  }

    public String getData() {  return LIB.ast_get_data(this.ptr);  }

    public Ast setData(final String data) {
        LIB.ast_set_data(this.ptr, data);
        return this;
    }

    public void clearData() {  LIB.ast_clear_data(this.ptr);  }

    public Ast addChild(final Ast child) {
        LIB.ast_add_child(this.ptr, child.ptr);
        return this;
    }

    public Ast getChild(final long index) {  return new Ast(LIB.ast_get_child(this.ptr, index));  }

    public List<Ast> getChildren() {
        final List<Ast> children = new ArrayList<>();
        final long numChildren = this.countChildren();
        for (long i = 0; i < numChildren; i++) {
            children.add(this.getChild(i));
        }
        return children;
    }

    public void clearChildren() {  LIB.ast_clear_children(this.ptr);  }

    public long countChildren() {  return LIB.ast_count_children(this.ptr);  }

    public boolean hasChildren() {  return this.countChildren() > 0; }


    private StringBuilder repeat(final long times, final String string) {
        final StringBuilder sb = new StringBuilder();
        for (long i = 0; i < times; i++) {  sb.append(string);  }
        return sb;
    }

    private String string(long indentLevel) {
        if (this.ptr == null) {  return "" + null;  }

        final StringBuilder sb = new StringBuilder()
                .append(repeat(indentLevel, "    "))
                .append(this.getName())
                .append("(");

        final String data = this.getData();
        if (data != null && !data.isEmpty()) {
            if (this.hasChildren()) {
                sb      .append("\n")
                        .append(repeat(indentLevel + 1, "    "))
                        .append("\"").append(data).append("\", ");
            } else {
                sb.append("\"").append(data).append("\"");
            }
        }

        for (final Ast child : this.getChildren()) {
            sb      .append("\n")
                    .append(child.string(indentLevel + 1))
                    .append(",");
        }

        if (this.hasChildren()) {
            sb      .append("\n")
                    .append(repeat(indentLevel, "    "))
                    .append(")");
        } else {
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.string(0);
    }
}
