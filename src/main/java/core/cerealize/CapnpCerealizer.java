package core.cerealize;

import amplifier.Msg;
import core.NotImplementedException;
import monto.Product;
import monto.Version;
import org.capnproto.*;

import java.io.IOException;
import java.nio.ByteBuffer;

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
        return null; // TODO:
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
