@0x8fd82cbf0aea2233;

using Java = import "/java.capnp";
$Java.package("org.capnproto.examples");
$Java.outerClassname("ProductOuter");

using Common = import "./common.capnp";

# A Product is a message that is, in order:
#  1. sent by a `Server`, based on a computation it performed on some `Version`
#  2. received by a broker
#  3. sent by that `Broker` to all listening `Sink`s.
#  4. received by one or more `Sink`s
struct Product {
    # An identifier indicating the Server that sent this product
    source @0 : Text;

    # A unique identifier indicating the originator of the `contents`,
    # for example the name of a file being edited. This is typically the
    # same value as the `Version` that caused the server response.
    product @1 : Text;

    # The language of a product. Useful for some products, e.g. ASTs.
    # However this is not a useful field for all languages so the field
    # can be set to a `none` variant.
    language @2 : Common.Language;

    # The content of this version. Note that this can be some dirty
    # representation of the `origin` data, for example a file being
    # edited that has changes not yet written to disk.
    contents @3 : Common.Contents;
}
