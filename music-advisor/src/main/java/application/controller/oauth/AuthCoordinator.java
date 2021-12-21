package application.controller.oauth;

import application.controller.Config;
import application.controller.oauth.exception.AuthException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.Thread.*;

public class AuthCoordinator {

    private final String authLink;
    private RedirectServer server;
    private String accessToken;

    private AuthCoordinator() {
        authLink = buildAuthLink();
    }

    public static AuthCoordinator create() {
        return new AuthCoordinator();
    }

    public String getAccessRequestLink() {
        return authLink;
    }

    public String getAccessToken() throws AuthException {
        try {
            return tryToReceiveAccessToken();
        } catch (IOException | InterruptedException e) {
            throw new AuthException(e.getMessage());
        }
    }

    private String tryToReceiveAccessToken() throws IOException, InterruptedException {
        waitForResponseToRedirectServer();
        sendRequestToGetAccessTokenByRedirect();

        return accessToken;
    }

    private void waitForResponseToRedirectServer() throws IOException {
        server = RedirectServer.create();
        server.startServer();
        waitingForCode();
        server.stopServer(1);
    }

    private void sendRequestToGetAccessTokenByRedirect() throws IOException, InterruptedException {
        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        buildAccessTokenRequestBody()
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(buildUriString()))
                .build();

        HttpResponse<String> responseWithAccessToken = HttpClient
                .newBuilder()
                .build()
                .send(requestForAccessToken, HttpResponse.BodyHandlers.ofString());

        accessToken = parseAccessToken(responseWithAccessToken.body());
    }

    private static String parseAccessToken(final String bearerToken) {
        JsonObject object = JsonParser.parseString(bearerToken).getAsJsonObject();
        return object.get("access_token").getAsString();
    }

    private void waitingForCode() {
        while (server.getCode() == null) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                assert false : "This will never happen";
            }
        }
    }

    private static String buildUriString() {
        StringBuilder builder = new StringBuilder();
        return builder.append(Config.AUTH_SERVER).append("/api/token")
                      .toString();
    }

    private String buildAccessTokenRequestBody() {
        StringBuilder builder = new StringBuilder();
        return builder.append("client_id=").append(Config.CLIENT_ID)
                      .append("&client_secret=").append(Config.CLIENT_SECRET)
                      .append("&grant_type=authorization_code")
                      .append("&code=").append(server.getCode())
                      .append("&redirect_uri=").append(Config.REDIRECT_URI)
                      .toString();
    }

    private static String buildAuthLink() {
        StringBuilder builder = new StringBuilder();
        return builder.append(Config.AUTH_SERVER)
                      .append("/authorize?client_id=").append(Config.CLIENT_ID)
                      .append("&redirect_uri=").append(Config.REDIRECT_URI)
                      .append("&response_type=code")
                      .toString();
    }
}