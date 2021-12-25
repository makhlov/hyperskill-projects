/* Class name: RedirectServer
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.oauth;

import java.io.IOException;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

/**
 * The server to which authorization code is sent from the Spotify OAuth.
 * @see <a href="https://oauth.net/2/grant-types/authorization-code/">OAuth 2.0 Authorization Code Grant</a>
 */
class RedirectServer {

    private final static String SUCCESS = "Got the code. Return back to your program.";
    private final static String FAILED = "Authorization code not found. Try again.";

    private final HttpServer server;
    private String code;

    /**
     * Private constructor.
     * @throws IOException if there are exchange problems (see <code>HttpServer</code> code for more info)
     */
    private RedirectServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        defineContext(server);
    }

    /**
     * Private constructor.
     * @throws IOException if there are exchange problems (see <code>HttpServer</code> code for more info)
     */
    public static RedirectServer create() throws IOException {
        return new RedirectServer();
    }

    /**
     * Get authorization code grant.
     * @return authorization code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Starts the server.
     */
    public void startServer() {
        server.start();
    }

    /**
     * Stops the server
     * @param delay the maximum time in seconds to wait until exchanges have finished.
     */
    public void stopServer(int delay) {
        server.stop(delay);
    }

    /**
     * Defines the root server request handler.
     * @param server the server to which the handler is associated.
     */
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