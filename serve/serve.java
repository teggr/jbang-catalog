///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS org.nanohttpd:nanohttpd-webserver:2.3.1

import fi.iki.elonen.SimpleWebServer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name = "serve", mixinStandardHelpOptions = true, version = "serve 0.1",
        description = "serve a directory over http")
class serve implements Callable<Integer> {

    @CommandLine.Option(names = {"-p","--port"}, description = "Port for serving the files within the directory", defaultValue = "80")
    private int port;

    @CommandLine.Option(names = {"-v","--verbose"}, description = "Turn on verbose logging", defaultValue = "false")
    private boolean verbose;

    @Parameters(index = "0", description = "The directory to serve. Will default to the current working directory", defaultValue = "")
    private File serveDirectory;

    public static void main(String... args) {
        int exitCode = new CommandLine(new serve()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        if(serveDirectory == null) {
            serveDirectory = new File("").getAbsoluteFile();
        }
        if(!serveDirectory.exists()) {
            return 1;
        }

        SimpleWebServer server = new SimpleWebServer("localhost", port, serveDirectory, !verbose);
        server.start();
        System.out.println("Server started on port " + port + ", serving files from: " + serveDirectory.getAbsolutePath());
        System.out.println("Press Enter to stop...");

        new Scanner(System.in).nextLine(); // Wait for Enter key press
        server.stop();
        System.out.println("Server stopped.");

        return 0;
    }

}
