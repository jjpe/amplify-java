package amplifier;

import core.*;
import core.cerealize.ICerealizer;
import org.capnproto.*;
import org.capnproto.Void;
import org.capnproto.examples.CommonOuter;
import org.capnproto.examples.MsgOuter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class Msg {
//    private ICerealizer cerealizer = new Msg.CapnpCerealizer();
//
//    /**
//     * An identifier indicating the Source that sent this version.
//     *
//     * Its value must never be <b>null</b>.
//     */
//    private String source;
//
//
//    /**
//     * A unique identifier. The Source indicated by the `source` field
//     * increments this number for each version it sends.
//     */
//    private long requestNumber;
//
//    /**
//     * Indicates the origin of the `contents`, usually the location of a file
//     * being edited.
//     *
//     * If the value is <b>null</b> then there is no origin associated with this
//     * Msg e.g. if the Msg originates from scratch code.
//     */
//    private String origin;
//
//    /**
//     * The content of this version. Note that this can be some dirty
//     * representation of the `origin` data, for example a file being
//     * edited that has changes not yet written to disk.
//     *
//     * If the value is <b>null</b> then there are no contents associated with this Msg.
//     */
//    private Contents contents;
//
//    /**
//     * This refers to 0 or more regions in the `contents`.
//     */
//    private List<Region> regions;
//
//    /**
//     * The language of this message. This can be the language of the original
//     * source code, or a language based on data derived from that source code.
//     *
//     * If the value is <b>null</b> then there is no language associated with this Msg.
//     */
//    private Language language;
//
//    /**
//     * An optional abstract syntax tree associated with the message.
//     *
//     * If the value is <b>null</b> then there is no AST associated with this Msg.
//     */
//    private Ast ast;
//
//
//    public Msg(final String source) {
//        this.source = source;
//        this.requestNumber = 0;
//        this.origin = null;
//        this.contents = null;
//        this.regions = new ArrayList<>();
//        this.language = null;
//        this.ast = null;
//    }
//
//    public Msg setSource(String source) {
//        this.source = source;
//        return this;
//    }
//
//    public Msg setRequestNumber(long requestNumber) {
//        this.requestNumber = requestNumber;
//        return this;
//    }
//
//    public Msg setOrigin(String origin) {
//        this.origin = origin;
//        return this;
//    }
//
//    public Msg setContents(Contents contents) {
//        this.contents = contents;
//        return this;
//    }
//
//    public Msg setRegions(List<Region> regions) {
//        this.regions = regions;
//        return this;
//    }
//
//    public Msg setLanguage(Language language) {
//        this.language = language;
//        return this;
//    }
//
//    public Msg setAst(Ast ast) {
//        this.ast = ast;
//        return this;
//    }
//
//
//    public void incrementRequestNumber() { this.requestNumber += 1; }
//
//    public void resetRequestNumber() { this.requestNumber = 0; }
//
//    public String getSource() { return this.source; }
//
//    public long getRequestNumber() { return this.requestNumber; }
//
//    public String getOrigin() { return this.origin; }
//
//    public Contents getContents() { return this.contents; }
//
//    public List<Region> getRegions() { return this.regions; }
//
//    public Language getLanguage() { return this.language; }
//
//    public Ast getAst() { return this.ast; }
//
//
//    public void setCerealizer(ICerealizer cerealizer) {
//        this.cerealizer = cerealizer;
//    }
//
//    public ICerealizer getCerealizer() { return this.cerealizer; }
//
//    static class CapnpCerealizer implements ICerealizer {
//        private static final int MB = 1024 * 1014;
//        private final byte[] CEREALIZE_BUFFER = new byte[64 * MB]; // TODO: get rid of the buffer.
//
//        public CapnpCerealizer() {
//            super();
//        }
//
//        @Override
//        public <T> byte[] cerealize(T msg) {
//            final MessageBuilder messageBuilder = new MessageBuilder();
//            final MsgOuter.Msg.Builder msgBuilder = messageBuilder.initRoot(MsgOuter.Msg.factory);
//            final Msg m = (Msg) msg;
//            msgBuilder.setSource(m.getSource());
//            msgBuilder.setRequestNumber(m.getRequestNumber());
//            cerealizeOrigin(m.getOrigin(), msgBuilder);
//            cerealizeContents(m.getContents(), msgBuilder);
//            cerealizeRegions(m.getRegions(), msgBuilder);
//            cerealizeLanguage(m.getLanguage(), msgBuilder);
//            cerealizeAst(m.getAst(), msgBuilder);
//
//            try {
//                // FIXME: this type of buffer use is rather fragile.
//                final ByteBuffer buffer = ByteBuffer.wrap(CEREALIZE_BUFFER);
//                buffer.clear();
//                final WritableByteChannel channel = new ArrayOutputStream(buffer);
//                SerializePacked.writeToUnbuffered(channel, messageBuilder);
//                return CEREALIZE_BUFFER;
//            } catch (IOException ioe) {
//                // TODO:
//                ioe.printStackTrace();
//                return null;
//            }
//        }
//
//        private static void cerealizeOrigin(final String origin, final MsgOuter.Msg.Builder msgBuilder) {
//            final CommonOuter.Option.Builder<Text.Builder> optionBuilder = msgBuilder.initOrigin();
//            if (origin == null) {
//                optionBuilder.setNone(Void.VOID);
//            } else {
//                optionBuilder.setSome(Text.factory, new Text.Reader(origin));
//            }
//        }
//
//        private static void cerealizeContents(final Contents contents, final MsgOuter.Msg.Builder msgBuilder) {
//            final CommonOuter.Option.Builder<CommonOuter.Contents.Builder> optionBuilder = msgBuilder.initContents();
//            if (contents == null) {
//                optionBuilder.setNone(Void.VOID);
//                return;
//            }
//            switch (contents.which()) {
//                case Text:
//                    final CommonOuter.Contents.Builder cb = optionBuilder.initSome();
//                    cb.setText(contents.asString());
//                    break;
//                case Entries:
//                    final List<String> entries = contents.asEntries();
//                    final int entryCount = entries.size();
//
//
//                    // TODO: This case  fails
//
//                    final CommonOuter.Contents.Builder contentsBuilder = optionBuilder.initSome(100000);
//
////                    contentsBuilder.setEntries();
////                    new CommonOuter.Contents.factory.
////                    final TextList.Builder builders = contentsBuilder.initEntries(2 * 1024 * 1024);
////                    contentsBuilder.set
//
//                    final TextList.Builder entriesBuilder = contentsBuilder.initEntries(entryCount);
//                    for (int i = 0; i < entryCount; i++) {
//                        final String entry = entries.get(i);
//                        entriesBuilder.set(i, new Text.Reader(entry));
//
////                        final CharBuffer charBuffer = entriesBuilder.get(i).asByteBuffer().asCharBuffer();
////                        charBuffer.clear();
////                        charBuffer.append(entry);
//
//                    }
//
////                    TextList.Builder
//
////                    optionBuilder.setSome(TextList.factory, entriesReader);
//                    break;
//                default:
//                    throw new NotImplementedException("Msg: Unknown Contents enum variant");
//            }
//        }
//
//        private static void cerealizeRegions(final List<Region> regions, final MsgOuter.Msg.Builder msgBuilder) {
//            final int regionCount = regions.size();
//            final StructList.Builder<CommonOuter.Region.Builder> regionsBuilder = msgBuilder.initRegions(regionCount);
//            for (int i = 0; i < regionCount; i++) {
//                final Region region = regions.get(i);
//                final CommonOuter.Region.Builder regionBuilder = regionsBuilder.get(i);
//                regionBuilder.setBegin(region.getBegin());
//                regionBuilder.setEnd(region.getEnd());
//            }
//        }
//
//        private static void cerealizeLanguage(final Language language, final MsgOuter.Msg.Builder msgBuilder) {
//            final CommonOuter.Option.Builder<CommonOuter.Language.Builder> languageOptionBuilder = msgBuilder.initLanguage();
//            if (language == null) {
//                languageOptionBuilder.setNone(Void.VOID);
//            } else {
//                final CommonOuter.Language.Builder languageBuilder = languageOptionBuilder.initSome();
//                languageBuilder.setRaw(language.getLanguage());
//            }
//
//        }
//
//        private static void cerealizeAst(final Ast ast, final MsgOuter.Msg.Builder msgBuilder) {
//            final CommonOuter.Option.Builder<CommonOuter.Ast.Builder> astOptionBuilder = msgBuilder.initAst();
//            if (ast == null) {
//                astOptionBuilder.setNone(Void.VOID);
//            } else {
//                cerealizeAstNode(ast, astOptionBuilder.initSome());
//            }
//        }
//
//        private static void cerealizeAstNode(final Ast ast, final CommonOuter.Ast.Builder astBuilder) {
//            astBuilder.setName(ast.getName());
//            final String data = ast.getData();
//            astBuilder.setData(data != null ? data : "");
//
//            final int childCount = ast.getChildren().size();
//            final StructList.Builder<CommonOuter.Ast.Builder> childrenBuilder = astBuilder.initChildren(childCount);
//            for (int i = 0; i < childCount; i++) {
//                final Ast child = ast.getChildren().get(i);
//                final CommonOuter.Ast.Builder childBuilder = childrenBuilder.get(i);
//                cerealizeAstNode(child, childBuilder);
//            }
//        }
//
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public <T> T decerealize(byte[] bytes) {
//            try {
//                final BufferedInputStream bis = new ArrayInputStream(ByteBuffer.wrap(bytes));
//                final MessageReader messageReader = SerializePacked.read(bis);
//                final MsgOuter.Msg.Reader msgReader = messageReader.getRoot(MsgOuter.Msg.factory);
//                return (T) new Msg(decerealizeSource(msgReader))
//                        .setRequestNumber(msgReader.getRequestNumber())
//                        .setOrigin(decerealizeOrigin(msgReader))
//                        .setContents(decerealizeContents(msgReader))
//                        .setRegions(decerealizeRegions(msgReader))
//                        .setLanguage(decerealizeLanguage(msgReader))
//                        .setAst(decerealizeAst(msgReader));
//            } catch (IOException ioe) {
//                throw new RuntimeException("SerializePacked read failed", ioe);
//            }
//        }
//
//        private static String decerealizeSource(final MsgOuter.Msg.Reader msgReader) {
//            final Text.Reader sourceReader = msgReader.getSource();
//            return sourceReader.toString();
//        }
//
//        private static String decerealizeOrigin(final MsgOuter.Msg.Reader msgReader) {
//            final CommonOuter.Option.Reader<Text.Reader> optionReader = msgReader.getOrigin();
//            switch(optionReader.which()) {
//                case NONE: break;
//                case SOME:
//                    final Text.Reader originReader = optionReader.getSome();
//                    return originReader.toString();
//                case _NOT_IN_SCHEMA:
//                    throw new NotImplementedException("Not in schema");
//            }
//            return null;
//        }
//
//        private static core.Contents decerealizeContents(final MsgOuter.Msg.Reader msgReader) {
//            final CommonOuter.Option.Reader<CommonOuter.Contents.Reader> optionReader = msgReader.getContents();
//            switch(optionReader.which()) {
//                case NONE: break;
//                case SOME:
//                    final CommonOuter.Contents.Reader contentsReader = optionReader.getSome();
//                    switch(contentsReader.which()) {
//                        case TEXT:
//                            final String text = contentsReader.getText().toString();
//                            return new core.Contents(text);
//                        case ENTRIES:
//                            final List<String> entries = new ArrayList<>();
//                            for (final Text.Reader entry : contentsReader.getEntries()) {
//                                entries.add(entry.toString());
//                            }
//                            return new core.Contents(entries);
//                        case _NOT_IN_SCHEMA:
//                            throw new NotImplementedException("Not in schema");
//                    }
//                case _NOT_IN_SCHEMA:
//                    throw new NotImplementedException("Not in schema");
//            }
//            return null;
//        }
//
//        private static List<core.Region> decerealizeRegions(final MsgOuter.Msg.Reader msgReader) {
//            final List<core.Region> regions = new ArrayList<>();
//            for (final CommonOuter.Region.Reader r : msgReader.getRegions()) {
//                regions.add(new core.Region(r.getBegin(), r.getEnd()));
//            }
//            return regions;
//        }
//
//        private static core.Language decerealizeLanguage(final MsgOuter.Msg.Reader msgReader) {
//            final CommonOuter.Option.Reader<CommonOuter.Language.Reader> optionReader = msgReader.getLanguage();
//            switch(optionReader.which()) {
//                case NONE: break;
//                case SOME:
//                    final CommonOuter.Language.Reader languageReader = optionReader.getSome();
//                    return new core.Language(languageReader.getRaw().toString());
//                case _NOT_IN_SCHEMA:
//                    throw new NotImplementedException("Not in schema");
//            }
//            return null;
//        }
//
//        private static core.Ast decerealizeAst(final MsgOuter.Msg.Reader msgReader) {
//            final CommonOuter.Option.Reader<CommonOuter.Ast.Reader> optionReader = msgReader.getAst();
//            switch(optionReader.which()) {
//                case NONE: break;
//                case SOME:
//                    final CommonOuter.Ast.Reader astReader = optionReader.getSome();
//                    return decerealizeAstNode(astReader);
//                case _NOT_IN_SCHEMA:
//                    throw new NotImplementedException("Not in schema");
//            }
//            return null;
//        }
//
//        private static core.Ast decerealizeAstNode(final CommonOuter.Ast.Reader astReader) {
//            final String name = astReader.getName().toString();
//            final String data = astReader.hasData()  ?  astReader.getData().toString()  :  "";
//            core.Ast ast = new core.Ast(name).addData(data);
//            for (final CommonOuter.Ast.Reader childReader : astReader.getChildren()) {
//                final core.Ast child = decerealizeAstNode(childReader);
//                ast = ast.addChildren(child);
//            }
//            return ast;
//        }
//    }
}
