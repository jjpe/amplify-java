package monto;

import core.Contents;
import core.Language;
import core.NotImplementedException;
import core.Region;
import core.cerealize.ICerealizer;
import org.capnproto.*;
import org.capnproto.examples.CommonOuter;
import org.capnproto.examples.VersionOuter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Version {
    private ICerealizer cerealizer = new Version.CapnpCerealizer();

    /**
     * An identifier indicating the Source that sent this version.
     *
     * Its value must never be <b>null</b>.
     */
    private String source;

    /**
     * The language of this message. This can be the language of the original
     * source code, or a language based on data derived from that source code.
     *
     * If the value is <b>null</b> then there is no language associated with this Msg.
     */
    private Language language;

    /**
     * The content of this version. Note that this can be some dirty
     * representation of the `origin` data, for example a file being
     * edited that has changes not yet written to disk.
     *
     * If the value is <b>null</b> then there are no contents associated with this Msg.
     */
    private Contents contents;

    /**
     * This refers to 0 or more selections in the `contents`.
     */
    private List<Region> selections;

    public Version(final String source) {
        this.source = source;
        this.language = null;
        this.contents = null;
        this.selections = null;
    }

    public Version setSource(String source) {
        this.source = source;
        return this;
    }

    public Version setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public Version setContents(Contents contents) {
        this.contents = contents;
        return this;
    }

    public Version setSelections(List<Region> selections) {
        this.selections = selections;
        return this;
    }


    public String getSource() { return this.source; }

    public Language getLanguage() { return this.language; }

    public Contents getContents() { return this.contents; }

    public List<Region> getSelections() { return this.selections; }


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
                final VersionOuter.Version.Reader versionReader =
                        messageReader.getRoot(VersionOuter.Version.factory);
                return (T) new Version(decerealizeSource(versionReader))
                        .setLanguage(decerealizeLanguage(versionReader))
                        .setContents(decerealizeContents(versionReader))
                        .setSelections(decerealizeSelections(versionReader));
            } catch (IOException ioe) {
                throw new RuntimeException("SerializePacked read failed", ioe);
            }
        }


        private static String decerealizeSource(final VersionOuter.Version.Reader msgReader) {
            final Text.Reader sourceReader = msgReader.getSource();
            return sourceReader.toString();
        }

        private static core.Language decerealizeLanguage(final VersionOuter.Version.Reader versionReader) {
            final CommonOuter.Language.Reader languageReader = versionReader.getLanguage();
            return new core.Language(languageReader.getRaw().toString());
        }

        private static core.Contents decerealizeContents(final VersionOuter.Version.Reader versionReader) {
            final CommonOuter.Contents.Reader contentsReader = versionReader.getContents();
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

        private static List<core.Region> decerealizeSelections(final VersionOuter.Version.Reader versionReader) {
            final StructList.Reader<CommonOuter.Region.Reader> regionsReader = versionReader.getSelections();
            final List<core.Region> regions = new ArrayList<>();
            for (CommonOuter.Region.Reader r : regionsReader) {
                regions.add(new core.Region(r.getBegin(), r.getEnd()));
            }
            return regions;
        }



    }
}
