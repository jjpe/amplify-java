package core.cerealize;

import amplifier.Msg;
import core.*;
import monto.Product;
import monto.Version;
import org.capnproto.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.capnproto.Text;
import org.capnproto.examples.CommonOuter.Ast;
import org.capnproto.examples.CommonOuter.Contents;
import org.capnproto.examples.CommonOuter.Language;
import org.capnproto.examples.CommonOuter.Region;
import org.capnproto.examples.MsgOuter;
import org.capnproto.examples.CommonOuter.*;

public class CapnpCerealizer implements ICerealizer {
    @Override
    public <T> byte[] cerealize(final T msg, final Class<T> clazz) {
        MessageBuilder messageBuilder = new MessageBuilder();
        if (Msg.class.equals(clazz)) {
            return CapnpCerealizer.cerealizeMsg((Msg) msg, messageBuilder);
        } else if (Version.class.equals(clazz)) {
            return CapnpCerealizer.cerealizeVersion((Version) msg, messageBuilder);
        } else if (Product.class.equals(clazz)) {
            return CapnpCerealizer.cerealizeProduct((Product) msg, messageBuilder);
        } else {
            throw new NotImplementedException(String.format("Unknown class: %s", clazz));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T decerealize(final byte[] bytes, final Class<T> clazz) {
        try {
            final BufferedInputStream bis = new ArrayInputStream(ByteBuffer.wrap(bytes));
            MessageReader messageReader = SerializePacked.read(bis);

            if (Msg.class.equals(clazz)) {
                return (T) CapnpCerealizer.decerealizeMsg(messageReader);
            } else if (Version.class.equals(clazz)) {
                return (T) CapnpCerealizer.decerealizeVersion(messageReader);
            } else if (Product.class.equals(clazz)) {
                return (T) CapnpCerealizer.decerealizeProduct(messageReader);
            } else {
                throw new NotImplementedException(String.format("Unknown class: %s", clazz));
            }
        } catch (IOException ioe) {
            throw new RuntimeException("SerializePacked read failed", ioe);
        }
    }


    private static Msg decerealizeMsg(final MessageReader messageReader) {
        final MsgOuter.Msg.Reader msgReader = messageReader.getRoot(MsgOuter.Msg.factory);

        final String source = decerealizeSource(msgReader);
        final long requestNumber = msgReader.getRequestNumber();
        final String origin = decerealizeOrigin(msgReader);
        final core.Contents contents = decerealizeContents(msgReader);
        final List<core.Region> regions = decerealizeRegions(msgReader);
        final core.Language language = decerealizeLanguage(msgReader);
        final core.Ast ast = decerealizeAst(msgReader);

        return new Msg(source)
                .setRequestNumber(requestNumber)
                .setOrigin(origin)
                .setContents(contents)
                .setRegions(regions)
                .setLanguage(language)
                .setAst(ast);
    }

    private static String decerealizeSource(final MsgOuter.Msg.Reader msgReader) {
        final Text.Reader sourceReader = msgReader.getSource();
        return sourceReader.toString();
    }

    private static String decerealizeOrigin(final MsgOuter.Msg.Reader msgReader) {
        final Option.Reader<Text.Reader> originReader = msgReader.getOrigin();
        switch(originReader.which()) {
            case NONE: break;
            case SOME:
                final Text.Reader reader = originReader.getSome();
                return reader.toString();
            case _NOT_IN_SCHEMA:
                throw new NotImplementedException("Not in schema");
        }
        return null;
    }

    private static core.Contents decerealizeContents(final MsgOuter.Msg.Reader msgReader) {
        final Option.Reader<Contents.Reader> contentsReader = msgReader.getContents();
        switch(contentsReader.which()) {
            case NONE: break;
            case SOME:
                final Contents.Reader reader = contentsReader.getSome();
                switch(reader.which()) {
                    case TEXT:
                        final String text = reader.getText().toString();
                        return new core.Contents(text);
                    case ENTRIES:
                        final List<String> entries = new ArrayList<>();
                        for (final Text.Reader entry : reader.getEntries()) {
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
        final StructList.Reader<Region.Reader> regionsReader = msgReader.getRegions();
        final List<core.Region> regions = new ArrayList<>();
        for (Region.Reader r : regionsReader) {
            regions.add(new core.Region(r.getBegin(), r.getEnd()));
        }
        return regions;
    }

    private static core.Language decerealizeLanguage(final MsgOuter.Msg.Reader msgReader) {
        final Option.Reader<Language.Reader> languageReader = msgReader.getLanguage();
        switch(languageReader.which()) {
            case NONE: break;
            case SOME:
                final Language.Reader reader = languageReader.getSome();
                return new core.Language(reader.getRaw().toString());
            case _NOT_IN_SCHEMA:
                throw new NotImplementedException("Not in schema");
        }
        return null;
    }

    private static core.Ast decerealizeAst(final MsgOuter.Msg.Reader msgReader) {
        final Option.Reader<Ast.Reader> astReader = msgReader.getAst();
        switch(astReader.which()) {
            case NONE: break;
            case SOME:
                final Ast.Reader reader = astReader.getSome();
                return decerealizeAstNode(reader);
            case _NOT_IN_SCHEMA:
                throw new NotImplementedException("Not in schema");
        }
        return null;
    }

    private static core.Ast decerealizeAstNode(final Ast.Reader astReader) {
        final String name = astReader.getName().toString();
        final String data = astReader.hasData()
                ? astReader.getData().toString()
                : "";
        core.Ast ast = new core.Ast(name).addData(data);
        for (final Ast.Reader childReader : astReader.getChildren()) {
            final core.Ast child = decerealizeAstNode(childReader);
            ast = ast.addChildren(child);
        }
        return ast;
    }

    private static Version decerealizeVersion(final MessageReader messageReader) {
        return null; // TODO:
    }

    private static Product decerealizeProduct(final MessageReader messageReader) {
        return null; // TODO:
    }


    private static byte[] cerealizeMsg(final Msg msg, final MessageBuilder messageBuilder) {
        return null; // TODO:
    }

    private static byte[] cerealizeVersion(final Version version, final MessageBuilder messageBuilder) {
        return null; // TODO:
    }

    private static byte[] cerealizeProduct(final Product product, final MessageBuilder messageBuilder) {
        return null; // TODO:
    }
}
