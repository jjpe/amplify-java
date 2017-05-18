package core;

import java.util.ArrayList;
import java.util.List;

public class Contents {
    public enum Which { Text, Entries }

    private String text;
    private List<String> entries;
    private Which which;

    public Contents(final String text) {
        this.text = text;
        this.entries = null;
        this.which = Which.Text;
    }

    public Contents(final List<String> entries) {
        this.text = null;
        this.entries = entries;
        this.which = Which.Entries;
    }

    public Which which() { return this.which; }

    public String asString() {
        switch(this.which) {
            case Text:
                return this.text;
            case Entries:
                final StringBuilder sb = new StringBuilder();
                for (final String entry : this.entries) {  sb.append(entry);  }
                return sb.toString();
            default: throw new NotImplementedException("Contents.asString(): Unknown variant " + this.which);
        }
    }

    public List<String> asEntries() {
        switch(this.which) {
            case Text:
                final List<String> list = new ArrayList<String>();
                list.add(this.text);
                return list;
            case Entries:
                return this.entries;
            default: throw new NotImplementedException("Contents.asEntries(): Unknown variant " + this.which);
        }
    }
}
