package REST;

import REST.utils.Uri;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class AdminServer {

    public static long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory
                .create(Uri.AdminServer.SERVER_URL);
        server.start();

        System.out.println("Server running!");

        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
}
