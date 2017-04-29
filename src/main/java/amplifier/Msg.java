package amplifier;

import core.*;
import core.cerealize.ICerealizer;
import org.capnproto.*;
import org.capnproto.examples.CommonOuter;
import org.capnproto.examples.MsgOuter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Msg {
    private ICerealizer cerealizer = new Msg.CapnpCerealizer();

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


    public void setCerealizer(ICerealizer cerealizer) {
        this.cerealizer = cerealizer;
    }

    public ICerealizer getCerealizer() { return this.cerealizer; }

    static class CapnpCerealizer implements ICerealizer {
        @Override
        public <T> byte[] cerealize(T msg) {
            final MessageBuilder messageBuilder = new MessageBuilder();

            // TODO:
            return new byte[0];
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T decerealize(byte[] bytes) {
            try {
                final BufferedInputStream bis = new ArrayInputStream(ByteBuffer.wrap(bytes));
                final MessageReader messageReader = SerializePacked.read(bis);
                final MsgOuter.Msg.Reader msgReader = messageReader.getRoot(MsgOuter.Msg.factory);
                return (T) new Msg(decerealizeSource(msgReader))
                        .setRequestNumber(msgReader.getRequestNumber())
                        .setOrigin(decerealizeOrigin(msgReader))
                        .setContents(decerealizeContents(msgReader))
                        .setRegions(decerealizeRegions(msgReader))
                        .setLanguage(decerealizeLanguage(msgReader))
                        .setAst(decerealizeAst(msgReader));
            } catch (IOException ioe) {
                throw new RuntimeException("SerializePacked read failed", ioe);
            }
        }

        private static String decerealizeSource(final MsgOuter.Msg.Reader msgReader) {
            final Text.Reader sourceReader = msgReader.getSource();
            return sourceReader.toString();
        }

        private static String decerealizeOrigin(final MsgOuter.Msg.Reader msgReader) {
            final CommonOuter.Option.Reader<Text.Reader> optionReader = msgReader.getOrigin();
            switch(optionReader.which()) {
                case NONE: break;
                case SOME:
                    final Text.Reader originReader = optionReader.getSome();
                    return originReader.toString();
                case _NOT_IN_SCHEMA:
                    throw new NotImplementedException("Not in schema");
            }
            return null;
        }

        private static core.Contents decerealizeContents(final MsgOuter.Msg.Reader msgReader) {
            final CommonOuter.Option.Reader<CommonOuter.Contents.Reader> optionReader = msgReader.getContents();
            switch(optionReader.which()) {
                case NONE: break;
                case SOME:
                    final CommonOuter.Contents.Reader contentsReader = optionReader.getSome();
                    switch(contentsReader.which()) {
                        case TEXT:
                            final String text = contentsReader.getText().toString();
                            return new core.Contents(text);
                        case ENTRIES:
                            final List<String> entries = new ArrayList<>();
                            for (final Text.Reader entry : contentsReader.getEntries()) {
                                entries.add(entry.toString());
                            }
                            return new core.Contents(entries);
                        case _NOT_IN_SCHEMA:
                            throw new NotImplementedException("Not in schema");
                    }
                case _NOT_IN_SCHEMA:
                    throw new NotImplementedException("Not in schema");
            }
            return null;
        }

        private static List<core.Region> decerealizeRegions(final MsgOuter.Msg.Reader msgReader) {
            final List<core.Region> regions = new ArrayList<>();
            for (final CommonOuter.Region.Reader r : msgReader.getRegions()) {
                regions.add(new core.Region(r.getBegin(), r.getEnd()));
            }
            return regions;
        }

        private static core.Language decerealizeLanguage(final MsgOuter.Msg.Reader msgReader) {
            final CommonOuter.Option.Reader<CommonOuter.Language.Reader> optionReader = msgReader.getLanguage();
            switch(optionReader.which()) {
                case NONE: break;
                case SOME:
                    final CommonOuter.Language.Reader languageReader = optionReader.getSome();
                    return new core.Language(languageReader.getRaw().toString());
                case _NOT_IN_SCHEMA:
                    throw new NotImplementedException("Not in schema");
            }
            return null;
        }

        private static core.Ast decerealizeAst(final MsgOuter.Msg.Reader msgReader) {
            final CommonOuter.Option.Reader<CommonOuter.Ast.Reader> optionReader = msgReader.getAst();
            switch(optionReader.which()) {
                case NONE: break;
                case SOME:
                    final CommonOuter.Ast.Reader astReader = optionReader.getSome();
                    return decerealizeAstNode(astReader);
                case _NOT_IN_SCHEMA:
                    throw new NotImplementedException("Not in schema");
            }
            return null;
        }

        private static core.Ast decerealizeAstNode(final CommonOuter.Ast.Reader astReader) {
            final String name = astReader.getName().toString();
            final String data = astReader.hasData()  ?  astReader.getData().toString()  :  "";
            core.Ast ast = new core.Ast(name).addData(data);
            for (final CommonOuter.Ast.Reader childReader : astReader.getChildren()) {
                final core.Ast child = decerealizeAstNode(childReader);
                ast = ast.addChildren(child);
            }
            return ast;
        }
    }
}
