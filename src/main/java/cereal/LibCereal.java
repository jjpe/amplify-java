package cereal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface LibCereal extends Library {
    String DYLIB_NAME = "libcereal_c_api";
    LibCereal INSTANCE = Native.loadLibrary(DYLIB_NAME, LibCereal.class);

    /************************** UClient **************************/
    Pointer uclient_new();
    void uclient_destroy(Pointer uclient);

    void uclient_serialize_using_capn_proto(final Pointer client);
    void uclient_serialize_using_json(final Pointer client);
    void uclient_set_rx_addr(final Pointer client, final String addr);
    void uclient_set_tx_addr(final Pointer client, final String addr);
    Pointer uclient_connect(final Pointer client);

    /************************** CClient **************************/
    void cclient_destroy(Pointer cclient);

    void cclient_send(final Pointer client, final Pointer msg);
    void cclient_receive(final Pointer client, final Pointer msg);

    /************************** Msg **************************/
    Pointer msg_new();
    void msg_destroy(Pointer msg);

    void msg_set_source(final Pointer msg, final String source);
    String msg_get_source(final Pointer msg);
    void msg_set_request_number(final Pointer msg, long reqno);
    long msg_get_request_number(final Pointer msg);
    void msg_set_origin(final Pointer msg, final String origin);
    String msg_get_origin(final Pointer msg);
    void msg_set_contents(final Pointer msg, final Pointer contents);
    Pointer msg_get_contents(final Pointer msg);
    void msg_add_region(final Pointer msg, final Pointer region);
    void msg_clear_regions(final Pointer msg);
    Pointer msg_get_region(final Pointer msg, final long index);
    long msg_count_regions(final Pointer msg);
    void msg_set_language(final Pointer msg, final Pointer language);
    Pointer msg_get_language(final Pointer msg);
    void msg_set_ast(final Pointer msg, final Pointer ast);
    Pointer msg_get_ast(final Pointer msg);

    /************************** Contents **************************/
    Pointer contents_new_text(final String text);
    Pointer contents_new_entries();
    void contents_destroy(final Pointer contents);

    boolean contents_is_text(final Pointer contents);
    void contents_add_text(final Pointer contents, final String text);
    String contents_get_text(final Pointer contents);
    boolean contents_is_entries(final Pointer contents);
    void contents_add_entry(final Pointer contents, final String entry);
    String contents_get_entry(final Pointer contents, final long index);
    long contents_count_entries(final Pointer ast);

    /************************** Region **************************/
    Pointer region_new(final long begin, final long end);
    void region_destroy(final Pointer region);

    long region_get_begin(final Pointer region);
    long region_get_end(final Pointer region);

    /************************** Language **************************/
    Pointer language_new(final String name);
    void language_destroy(final Pointer language);

    String language_get_name(final Pointer language);
    void language_set_name(final Pointer language, final String name);

    /************************** Ast **************************/
    Pointer ast_new(final String name);
    void ast_destroy(final Pointer ast);

    String ast_get_name(final Pointer ast);
    void ast_set_data(final Pointer ast, final String data);
    String ast_get_data(final Pointer ast);
    void ast_clear_data(final Pointer ast);
    void ast_add_child(final Pointer ast, final Pointer child_ast);
    Pointer ast_get_child(final Pointer ast, final long index);
    void ast_clear_children(final Pointer ast);
    long ast_count_children(final Pointer ast);
}
