package cereal;

import java.util.Properties;

public class Demo {
    private static void printMsg(final String tag, final Msg msg) {
        System.out.println(String.format("%s msg.process:   '%s'", tag, msg.getProcess()));
        System.out.println(String.format("%s msg.reqno:     %s", tag, msg.getRequestNumber()));
        System.out.println(String.format("%s msg.kind:      '%s'", tag, msg.getKind()));
        System.out.println(String.format("%s msg.origin:    '%s'", tag, msg.getOrigin()));
        System.out.println(String.format("%s msg.contents:  '%s'", tag, msg.getContents()));
        System.out.println(String.format("%s msg.regions:   %s", tag, msg.getRegions()));
        System.out.println(String.format("%s msg.language:  '%s'", tag, msg.getLanguage().getName()));
        final Ast ast = msg.getAst();
        final String fmt = (ast == null)  ?  "%s msg.ast:       %s"  :  "%s msg.ast:       \n%s";
        System.out.println(String.format(fmt, tag, ast));
    }

    public static void main(final String[] args) {
        final Properties props = System.getProperties();
        props.setProperty("jna.library.path", "./src/main/resources");

        new Thread(() -> { // Source
            try {
                final CClient source = new UClient().connect();
                final Msg msg0 = new Msg()
                        .setOrigin("code (msg0)")
                        .setProcess("Java Test Source")
                        .setRequestNumber(20000)
                        .setKind("test msg")
                        .setLanguage(null)
                        .setLanguage(new Language("FlibberClabber"))
                        .setContents(Contents.newText("gah blah"))
                        .addRegions(new Region(0, 10),  new Region(10, 20))
                        .setAst(
                                new Ast("Plus").addChildren(
                                        new Ast("Integer").setData("2"),
                                        new Ast("Integer").setData("3"),
                                        new Ast("Mul").addChildren(
                                                new Ast("Integer").setData("7"),
                                                new Ast("Integer").setData("5"),
                                                new Ast("FnCall").setData("double").addChildren(
                                                        new Ast("Integer").setData("10"))))
                        );
                final Msg msg1 = new Msg()
                        .setOrigin("code (msg1)")
                        .setProcess("Java Test Source")
                        .setRequestNumber(20001)
                        .setKind("test msg")
                        .setLanguage(new Language("JibberJabber"))
                        .setContents(Contents.newEntries("Ėñtrÿ 0", "Ėñtrÿ 1"));
                while (true) {
                    Thread.sleep(10000);  // Delay sending to allow the network to settle
                    source.send(msg0.setRequestNumber(msg0.getRequestNumber() + 1));
                    System.out.println(String.format("[source] Sent msg0[%s]", msg0.getRequestNumber()));

                }
//                source.send(msg1);
//                System.out.println("[source] Sent msg1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })
                .start()
        ;

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
        })
//                .start()
        ;


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
