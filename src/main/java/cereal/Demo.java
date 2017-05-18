package cereal;

import java.util.Properties;

public class Demo {
    public static void main(final String[] args) {
        final Properties props = System.getProperties();
        props.setProperty("jna.library.path", "./native");

        new Thread(() -> { // Source
            try {
                final CClient source = new UClient().connect();
                final Msg msg0 = new Msg()
                        .setOrigin("code")
                        .setSource("Java Test Source")
                        .setLanguage(new Language("foo"))
                        .setRequestNumber(20000);
                Thread.sleep(1000);  // Delay sending to allow the network to settle
                source.send(msg0);
                System.out.println("[source] Sent a msg");
                final Msg msg1 = new Msg()
                        .setOrigin("code")
                        .setSource("Java Test Source")
                        .addRegion(new Region(0, 10))
                        .addRegion(new Region(10, 20))
//                        .setLanguage(new Language("-"))
                        .setRequestNumber(20001);
                source.send(msg1);
                System.out.println("[source] Sent a msg");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> { // Sink
            final Msg msg = new Msg();
            System.out.println(String.format("[sink] msg.source: '%s'", msg.getSource()));
            System.out.println(String.format("[sink] msg.reqno: %s", msg.getRequestNumber()));
            System.out.println(String.format("[sink] msg.origin: '%s'", msg.getOrigin()));
            System.out.println(String.format("[sink] msg.contents: %s", msg.getContents()));
            System.out.println(String.format("[sink] msg.regions: %s", msg.getRegions()));
            System.out.println(String.format("[sink] msg.language: '%s'", msg.getLanguage().getName()));
            System.out.println(String.format("[sink] msg.ast: '%s'", msg.getAst()));
            final CClient sink = new UClient().connect();
            System.out.println("[sink] connected");
            sink.receive(msg);
            System.out.println();
            System.out.println("[sink] received msg");
            System.out.println(String.format("[sink] msg.source: '%s'", msg.getSource()));
            System.out.println(String.format("[sink] msg.reqno: %s", msg.getRequestNumber()));
            System.out.println(String.format("[sink] msg.origin: '%s'", msg.getOrigin()));
            System.out.println(String.format("[sink] msg.contents: %s", msg.getContents()));
            System.out.println(String.format("[sink] msg.regions: %s", msg.getRegions()));
            System.out.println(String.format("[sink] msg.language: '%s'", msg.getLanguage().getName()));
            System.out.println(String.format("[sink] msg.ast: '%s'", msg.getAst()));
            sink.receive(msg);
            System.out.println();
            System.out.println("[sink] received msg");
            System.out.println(String.format("[sink] msg.source: '%s'", msg.getSource()));
            System.out.println(String.format("[sink] msg.reqno: %s", msg.getRequestNumber()));
            System.out.println(String.format("[sink] msg.origin: '%s'", msg.getOrigin()));
            System.out.println(String.format("[sink] msg.contents: %s", msg.getContents()));
            System.out.println(String.format("[sink] msg.regions: %s", msg.getRegions()));
            System.out.println(String.format("[sink] msg.language: '%s'", msg.getLanguage().getName()));
            System.out.println(String.format("[sink] msg.ast: '%s'", msg.getAst()));
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
