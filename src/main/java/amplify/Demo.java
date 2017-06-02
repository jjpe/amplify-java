package amplify;

public class Demo {
    static {
        // Ensure that the JAR generated from this code can find the dynamic lib
        System.setProperty("jna.library.path", "./src/main/resources/darwin");
    }

    private static void printMsg(final String tag, final Msg msg) {
        System.out.println(String.format("%s %s", tag, msg.toString()));
    }

    public static void main(final String[] args) {
        final Thread sourceThread = new Thread(() -> { // Source
            try {
                final CClient source = new UClient().connect();
                final Msg msg = new Msg()
                        .setOrigin("code (msg0)")
                        .setProcess("Java Test Source")
                        .setRequestNumber(20000)
                        .setKind("test msg")
                        .setLanguage(null)
                        .setLanguage(new Language("FlibberClabberese"))
                        .setContents(Contents.newText("gah blah"))
                        .addRegions(new Region(0, 10), new Region(10, 20))
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
                while (true) {
                    Thread.sleep(10000);  // Delay sending to allow the network to settle
                    source.send(msg.setRequestNumber(msg.getRequestNumber() + 1));
                    System.out.println(String.format("[source] Sent msg0[%s]", msg.getRequestNumber()));

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread sinkThread = new Thread(() -> { // Sink
            final Msg msg = new Msg();
            final CClient sink = new UClient().connect();
            System.out.println("[sink] connected");
            printMsg("[sink]", msg);

            while (true) {
                sink.receive(msg);
                printMsg("[sink]", msg);
            }
        });

        sourceThread.start();
        sinkThread.start();
    }
}
