package core.cerealize;

public interface ICerealizer {
    <T> byte[] cerealize(final T msg);

    <T> T decerealize(final byte[] bytes);
}
