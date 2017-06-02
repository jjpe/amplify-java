package amplify;

import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy convenience class to manipulate libamplify messages.
 */
public class Msg implements Closeable {
    private static final LibAmplify LIB = LibAmplify.INSTANCE;

    final Pointer ptr;

    public Msg() {  this.ptr = LIB.msg_new();  }

    public Msg setProcess(final String process) {
        LIB.msg_set_process(this.ptr, process);
        return this;
    }

    public String getProcess() {  return LIB.msg_get_process(this.ptr);  }

    public Msg setRequestNumber(long requestNumber) {
        LIB.msg_set_request_number(this.ptr, requestNumber);
        return this;
    }

    public long getRequestNumber() {  return LIB.msg_get_request_number(this.ptr);  }

    public Msg setKind(final String kind) {
        LIB.msg_set_kind(this.ptr, kind);
        return this;
    }

    public String getKind() {  return LIB.msg_get_kind(this.ptr);  }

    public Msg setOrigin(final String origin) {
        LIB.msg_set_origin(this.ptr, origin);
        return this;
    }

    public String getOrigin() {  return LIB.msg_get_origin(this.ptr);  }

    public Msg setContents(final Contents contents) {
        LIB.msg_set_contents(this.ptr, contents.ptr);
        return this;
    }

    public Contents getContents() {  return new Contents(LIB.msg_get_contents(this.ptr));  }

    public Msg addRegions(final Region... regions) {
        for (final Region region : regions) {
            LIB.msg_add_region(this.ptr, region.ptr);
        }
        return this;
    }

    public Msg clearRegions() {
        LIB.msg_clear_regions(this.ptr);
        return this;
    }

    public Region getRegion(final long index) {
        assert(index < this.countRegions());
        return new Region(LIB.msg_get_region(this.ptr, index));
    }

    public long countRegions() {  return LIB.msg_count_regions(this.ptr);  }

    public List<Region> getRegions() {
        final List<Region> regions = new ArrayList<>();
        final long numRegions = this.countRegions();
        for (long i = 0; i < numRegions; i++) {
            regions.add(this.getRegion(i));
        }
        return regions;
    }

    public Msg setLanguage(final Language language) {
        LIB.msg_set_language(this.ptr, language == null ? null : language.ptr);
        return this;
    }

    public Language getLanguage() {  return new Language(LIB.msg_get_language(this.ptr));  }

    public Msg setAst(final Ast ast) {
        LIB.msg_set_ast(this.ptr, ast.ptr);
        return this;
    }

    public Ast getAst() {
        final Pointer p = LIB.msg_get_ast(this.ptr);
        if (p == null) { return null; }
        return new Ast(p);
    }



    public void destroy() {  LIB.msg_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Msg {").append("\n");
        sb.append("    process = \"").append(this.getProcess()).append("\",\n");
        sb.append("    request number = ").append(this.getRequestNumber()).append(",\n");
        sb.append("    kind = \"").append(this.getKind()).append("\",\n");
        sb.append("    origin = ").append(this.getOrigin()).append(",\n");

        final Contents contents = this.getContents();
        if (contents == null) {
            sb.append("    contents = null,\n");
        } else {
            sb.append("    contents = ").append(contents).append(",\n");
        }
        sb.append("    regions = ").append(this.getRegions()).append(",\n");
        final Language language = this.getLanguage();
        if (language == null) {
            sb.append("    language = null,\n");
        } else {
            sb.append("    language = \"").append(language).append("\",\n");
        }
        final Ast ast = this.getAst();
        if (ast == null) {
            sb.append("    ast = null").append("\n");
        } else {
            sb.append("    ast =\n").append(ast.indentedString(2)).append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
