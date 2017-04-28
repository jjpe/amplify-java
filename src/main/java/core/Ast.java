package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ast {
    private final String name;
    private StringBuilder data;
    private List<Ast> children;

    public Ast(String name) {
        this.name = name;
        this.data = null;
        this.children = null;
    }

    public String getName() { return this.name; }

    public String getData() { return this.data.toString(); }

    public List<Ast> getChildren() { return this.children; }

    public Ast addData(final String data) {
        if (this.data == null) { this.data = new StringBuilder(); }
        this.data.append(data);
        return this;
    }

    public Ast addChildren(final Ast... children) {
        if (this.children == null) { this.children = new ArrayList<>(); }
        this.children.addAll(Arrays.asList(children));
        return this;
    }
}
