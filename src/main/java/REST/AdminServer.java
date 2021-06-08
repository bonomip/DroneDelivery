package REST;

import REST.utils.Uri;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

/**
 * Created by civi on 26/04/16.
 */
public class AdminServer {

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
