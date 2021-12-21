package application.controller.oauth;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RedirectServer {

    private final static String SUCCESS = "Got the code. Return back to your program.";
    private final static String FAILED = "Authorization code not found. Try again.";

    private final HttpServer server;
    private String code;

    private RedirectServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        defineContext(server);
    }

    public static RedirectServer create() throws IOException {
        return new RedirectServer();
    }

    public String getCode() {
        return code;
    }

    public void startServer() {
        server.start();
    }
    
    public void stopServer(int delay) {
        server.stop(delay);
    }

    private void defineContext(HttpServer server) {
        server.createContext("/", rootHandler -> {
            String query = rootHandler.getRequestURI().getQuery();

            String response;
            if (query != null && query.contains("code")) {
                code = query.replace("code=", "");
                response = SUCCESS;
            } else {
                response = FAILED;
            }

            rootHandler.sendResponseHeaders(200, response.length());
            rootHandler.getResponseBody().write(response.getBytes());
            rootHandler.getResponseBody().close();
        });
        server.setExecutor(null);
    }
}