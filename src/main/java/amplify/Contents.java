package amplify;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Contents implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    final Pointer ptr;

    Contents(final Pointer ptr) {  this.ptr = ptr;  }

    public static Contents newText(final String text) {
        return new Contents(LIB.contents_new_text(text));
    }

    public static Contents newEntries(final String... entries) {
        final Contents contents = new Contents(LIB.contents_new_entries());
        for (final String entry : entries) {
            contents.addEntry(entry);
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

    public boolean isEmpty() { return LIB.contents_is_empty(this.ptr); }

    @Override
    public String toString() {
        if (this.ptr == null) {  return "" + null;  }
        if (this.isText()) {  return this.getText();  }
        if (this.isEntries()) {
            final StringBuilder sb = new StringBuilder();
            final List<String> entries = this.getEntries();
            sb.append("[\n");
            for (int i = 0; i < entries.size(); i++) {
                final String entry = entries.get(i);
                sb.append("  ").append(entry);
                if (i < entries.size() - 1) {  sb.append(",");  }
                sb.append("\n");
            }
            sb.append("]");
            return sb.toString();
        }

        throw new RuntimeException("Contents is neither Text nor Entries");

    }
}
