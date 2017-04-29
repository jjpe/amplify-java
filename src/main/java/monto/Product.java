package monto;

import core.Contents;
import core.Language;
import core.NotImplementedException;
import core.cerealize.ICerealizer;
import org.capnproto.*;
import org.capnproto.examples.CommonOuter;
import org.capnproto.examples.ProductOuter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private ICerealizer cerealizer = new Product.CapnpCerealizer();

    /**
     * An identifier indicating the Source that sent this version.
     *
     * Its value must never be <b>null</b>.
     */
    private String source;

    /**
     *
     */
    private String product;

    /**
     * The language of this message. This can be the language of the original
     * source code, or a language based on data derived from that source code.
     *
     * If the value is <b>null</b> then there is no language associated with this Msg.
     */
    private Language language;

    /**
     * The content of this Product. Note that this can be some dirty
     * representation of the `origin` data, for example a file being
     * edited that has changes not yet written to disk.
     *
     * If the value is <b>null</b> then there are no contents associated with this Msg.
     */
    private Contents contents;


    public Product(final String source) {
        this.source = source;
        this.product = null;
        this.language = null;
        this.contents = null;
    }

    public Product setSource(String source) {
        this.source = source;
        return this;
    }

    public Product setProduct(String origin) {
        this.product = origin;
        return this;
    }

    public Product setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public Product setContents(Contents contents) {
        this.contents = contents;
        return this;
    }


    public String getSource() { return this.source; }

    public String getProduct() { return this.product; }

    public Language getLanguage() { return this.language; }

    public Contents getContents() { return this.contents; }


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
                final ProductOuter.Product.Reader productReader =
                        messageReader.getRoot(ProductOuter.Product.factory);
                return (T) new Product(decerealizeSource(productReader))
                        .setLanguage(decerealizeLanguage(productReader))
                        .setContents(decerealizeContents(productReader))
                        .setProduct(decerealizeProductField(productReader));
            } catch (IOException ioe) {
                throw new RuntimeException("SerializePacked read failed", ioe);
            }
        }

        private static String decerealizeSource(final ProductOuter.Product.Reader productReader) {
            final Text.Reader sourceReader = productReader.getSource();
            return sourceReader.toString();
        }

        private static core.Language decerealizeLanguage(final ProductOuter.Product.Reader productReader) {
            final CommonOuter.Language.Reader languageReader = productReader.getLanguage();
            return new core.Language(languageReader.getRaw().toString());
        }

        private static core.Contents decerealizeContents(final ProductOuter.Product.Reader productReader) {
            final CommonOuter.Contents.Reader contentsReader = productReader.getContents();
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
            return null;
        }

        private static String decerealizeProductField(final ProductOuter.Product.Reader productReader) {
            return productReader.getProduct().toString();
        }
    }
}
