package amplifier;

import core.Ast;
import core.Contents;
import core.Language;
import core.Region;

import java.util.List;

public class Msg {
    /**
     * An identifier indicating the Source that sent this version.
     *
     * Its value must never be <b>null</b>.
     */
    private String source;


    /**
     * A unique identifier. The Source indicated by the `source` field
     * increments this number for each version it sends.
     */
    private long requestNumber;

    /**
     * Indicates the origin of the `contents`, usually the location of a file
     * being edited.
     *
     * If the value is <b>null</b> then there is no origin associated with this
     * Msg e.g. if the Msg originates from scratch code.
     */
    private String origin;

    /**
     * The content of this version. Note that this can be some dirty
     * representation of the `origin` data, for example a file being
     * edited that has changes not yet written to disk.
     *
     * If the value is <b>null</b> then there are no contents associated with this Msg.
     */
    private Contents contents;

    /**
     * This refers to 0 or more regions in the `contents`.
     */
    private List<Region> regions;

    /**
     * The language of this message. This can be the language of the original
     * source code, or a language based on data derived from that source code.
     *
     * If the value is <b>null</b> then there is no language associated with this Msg.
     */
    private Language language;

    /**
     * An optional abstract syntax tree associated with the message.
     *
     * If the value is <b>null</b> then there is no AST associated with this Msg.
     */
    private Ast ast;


    public Msg(final String source) {
        this.source = source;
        this.requestNumber = 0;
        this.origin = null;
        this.contents = null;
        this.regions = null;
        this.language = null;
        this.ast = null;
    }

    public Msg setSource(String source) {
        this.source = source;
        return this;
    }

    public Msg setRequestNumber(long requestNumber) {
        this.requestNumber = requestNumber;
        return this;
    }

    public Msg setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public Msg setContents(Contents contents) {
        this.contents = contents;
        return this;
    }

    public Msg setRegions(List<Region> regions) {
        this.regions = regions;
        return this;
    }

    public Msg setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public Msg setAst(Ast ast) {
        this.ast = ast;
        return this;
    }


    public void incrementRequestNumber() { this.requestNumber += 1; }

    public void resetRequestNumber() { this.requestNumber = 0; }

    public String getSource() { return this.source; }

    public long getRequestNumber() { return this.requestNumber; }

    public String getOrigin() { return this.origin; }

    public Contents getContents() { return this.contents; }

    public List<Region> getRegions() { return this.regions; }

    public Language getLanguage() { return this.language; }

    public Ast getAst() { return this.ast; }
}
