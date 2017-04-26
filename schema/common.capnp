@0x92229de76728c225;

using Java = import "/java.capnp";
$Java.package("org.capnproto.examples");
$Java.outerClassname("CommonOuter");

struct Ast {
    name @0 : Text;
    data @1 : Text;
    children @2 : List(Ast);
}

struct Contents {
    union {
        text @0 : Text;
        entries @1 : List(Text);
    }
}

struct Language {
    raw @0 : Text;
}

# A region in some body of text is a 0-indexed half-open interval [begin, end).
struct Region {
    begin @0 : UInt64;
    end @1 : UInt64;
}

struct Uri {
    raw @0 : Text;
}

struct Option(T) {
    union {
        none @0 : Void;
        some @1 : T;
    }
}
