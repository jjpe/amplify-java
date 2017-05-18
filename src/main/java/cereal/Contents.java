package cereal;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Contents implements Closeable {
    private static final LibCereal LIB = LibCereal.INSTANCE;

    final Pointer ptr;

    Contents(final Pointer ptr) {  this.ptr = ptr;  }

    public static Contents newText(final String text) {
        final Contents contents = new Contents(LIB.contents_new_text(text));
        LIB.contents_add_text(contents.ptr, text);
        return contents;
    }

    public static Contents newEntries(final String... entries) {
        final Contents contents = new Contents(LIB.contents_new_entries());
        for (final String entry : entries) {
            LIB.contents_add_entry(contents.ptr, entry);
        }
        return contents;
    }

    public void destroy() {  LIB.contents_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

    public boolean isText() {  return LIB.contents_is_text(this.ptr);  }

    public void addText(final String text) {  LIB.contents_add_text(this.ptr, text);  }

    public String getText() {  return LIB.contents_get_text(this.ptr);  }


    public boolean isEntries() {  return LIB.contents_is_entries(this.ptr);  }

    public void addEntry(final String entry) {  LIB.contents_add_entry(this.ptr, entry);  }

    public String getEntry(final long index) {  return LIB.contents_get_entry(this.ptr, index);  }

    public long countEntries() {  return LIB.contents_count_entries(this.ptr);  }

    public List<String> getEntries() {
        final List<String> entries = new ArrayList<>();
        final long numEntries = this.countEntries();
        for (long i = 0; i < numEntries; i++) {
            entries.add(LIB.contents_get_entry(this.ptr, i));
        }
        return entries;
    }



//    boolean contents_is_entries(final Pointer contents);
//    void contents_add_entry(final Pointer contents, final String entry);
//    String contents_get_entry(final Pointer contents, final long index);
//    long ast_count_entries(final Pointer ast);

}
