@0xa471c481c2b803c0;

using Java = import "/java.capnp";
$Java.package("org.capnproto.examples");
$Java.outerClassname("MsgOuter");

using Common = import "common.capnp";
using import "common.capnp".Option;


struct Msg {
    # An identifier indicating the Source that sent this version
    source @0 : Text;

    # A unique identifier. The Source indicated by the `source` field
    # increments this number for each version it sends.
    requestNumber @1 : UInt64;

    # Indicates the origin of the `contents`, usually the location of a file
    # being edited.
    origin @2 : Option(Text);

    # The content of this version. Note that this can be some dirty
    # representation of the `origin` data, for example a file being
    # edited that has changes not yet written to disk.
    contents @3 : Option(Common.Contents);

    # This refers to 0 or more regions in the `contents`.
    regions @4 : List(Common.Region);

    # The language of this message. This can be the language of the original
    # source code, or a language based on data derived from that source code.
    language @5 : Option(Common.Language);

    # An optional abstract syntax tree associated with the message.
    ast @6 : Option(Common.Ast);
}
