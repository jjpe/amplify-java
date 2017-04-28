package core.cerealize;

public interface ICerealizer {
    public <T> byte[] cerealize(final T msg, final Class<T> clazz);

    <T> T decerealize(final byte[] bytes, Class<T> clazz);
}
