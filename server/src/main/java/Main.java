import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        new Server().run(port);
        System.out.printf("Server started on port %d%n", port);
    }
}