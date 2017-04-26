@0xc7111aff4d60b977;

using Java = import "/java.capnp";
$Java.package("org.capnproto.examples");
$Java.outerClassname("VersionOuter");

using Common = import "common.capnp";

# A Version is a message that is, in order:
#  1. sent by a `Source`
#  2. received by a `Broker`
#  3. sent by that `Broker` to all listening `Server`s
#  4. received by one or more `Server`s
struct Version {
    # An identifier indicating the Source that sent this version
    source @0 : Text;

    # A unique identifier. The Source indicated by the `source` field
    # increments this number for each version it sends.
    requestNumber @1 : UInt64;

    # A unique identifier indicating the originator of the `contents`,
    # for example the name of a file being edited.
    origin @2 : Text;

    # The content of this version. Note that this can be some dirty
    # representation of the `origin` data, for example a file being
    # edited that has changes not yet written to disk.
    contents @3 : Common.Contents;

    # Any selections made in the `contents`. Each selection is
    # expressed as a `Common`.`Region`.
    selections @4 : List(Common.Region);

    language @5 : Common.Language;
}
