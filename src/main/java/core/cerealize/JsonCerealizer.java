package core.cerealize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

public class JsonCerealizer implements ICerealizer {
    private final static String ENCODING = "UTF-8";

    private Gson gson;

    public JsonCerealizer() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public <T> byte[] cerealize(final T msg, final Class<T> _) {
        try {
            final String json = gson.toJson(msg, new TypeToken<Collection<T>>(){}.getType());
            return json.getBytes(ENCODING);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("JsonCerealizer.cerealize", uee);
        }
    }

    @Override
    public <T> T decerealize(final byte[] bytes, final Class<T> _) {
        try {
            final String json = new String(bytes, ENCODING);
            return gson.fromJson(json, new TypeToken<Collection<T>>(){}.getType());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("JsonCerealizer.decerealize", uee);
        }
    }
}
