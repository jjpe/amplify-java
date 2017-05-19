package cereal;

import java.util.Properties;

public class Demo {
    private static void printMsg(final String tag, final Msg msg) {
        System.out.println(String.format("%s msg.source:    '%s'", tag, msg.getSource()));
        System.out.println(String.format("%s msg.reqno:     %s", tag, msg.getRequestNumber()));
        System.out.println(String.format("%s msg.origin:    '%s'", tag, msg.getOrigin()));
        System.out.println(String.format("%s msg.contents:  '%s'", tag, msg.getContents()));
        System.out.println(String.format("%s msg.regions:   %s", tag, msg.getRegions()));
        System.out.println(String.format("%s msg.language:  '%s'", tag, msg.getLanguage().getName()));
        final Ast ast = msg.getAst();
        if (ast.ptr == null) {
            System.out.println(String.format("%s msg.ast:       %s", tag, null));
        } else {
            System.out.println(String.format("%s msg.ast:       \n%s", tag, ast));
        }
    }

    public static void main(final String[] args) {
        final Properties props = System.getProperties();
        props.setProperty("jna.library.path", "./native");

        new Thread(() -> { // Source
            try {
                final CClient source = new UClient().connect();
                final Msg msg0 = new Msg()
                        .setOrigin("code (msg0)")
                        .setRequestNumber(20000)
                        .setSource("Java Test Source")
                        .setLanguage(null)
                        .setContents(Contents.newText("gah blah"))
                        .setAst(
                                new Ast("Plus")
                                        .addChild(new Ast("Integer").setData("2"))
                                        .addChild(new Ast("Integer").setData("3"))
                                        .addChild(
                                                new Ast("Mul")
                                                        .addChild(new Ast("Integer").setData("7"))
                                                        .addChild(new Ast("Integer").setData("5"))
                                        )
                        );
                final Msg msg1 = new Msg()
                        .setOrigin("code (msg1)")
                        .setRequestNumber(20001)
                        .setSource("Java Test Source")
                        .addRegion(new Region(0, 10))
                        .addRegion(new Region(10, 20))
                        .setLanguage(new Language("JibberJabber"))
                        .setContents(Contents.newEntries(
                                "Ėñtrÿ 0",
                                "Ėñtrÿ 1"
                        ));
                Thread.sleep(1000);  // Delay sending to allow the network to settle
                source.send(msg0);
                System.out.println("[source] Sent msg0");
                source.send(msg1);
                System.out.println("[source] Sent msg1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> { // Sink
            final Msg msg = new Msg();
            final CClient sink = new UClient().connect();
            System.out.println("[sink] connected");
            printMsg("[sink]", msg);
            sink.receive(msg);
            System.out.println("\n[sink] received msg");
            printMsg("[sink]", msg);
            sink.receive(msg);
            System.out.println("\n[sink] received msg");
            printMsg("[sink]", msg);
        }).start();


//        final UClient client = new UClient("test client")
//                .connect();

//        final List<String> entries = new ArrayList<>();
//        entries.add("foo bar baz!");
//        entries.add("qux quux corge grault.");
//
//        final List<Region> regions = new ArrayList<>();
//        regions.add(new Region(0, 1));
//        regions.add(new Region(10, 11));
//
//        final Ast ast = new Ast("Sum");
//        ast.addChildren(
//                new Ast("Int").addData("2"),
//                new Ast("Int").addData("3")
//        );
//
//        final Msg msg = new Msg("Msg Source")
//                .setRequestNumber(9001)
//                .setOrigin("client tester (java) origin")
////                .setContents(new Contents("flah"))
////                .setContents(null)
//                .setContents(new Contents(entries)) // TODO: this fails ATM
//                .setRegions(regions)
//                .setLanguage(new Language("JibberJabber"))
//                .setAst(ast);
//        client.send(msg, 0);
    }
}
