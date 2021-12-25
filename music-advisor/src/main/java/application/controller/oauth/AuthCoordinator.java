/* Class name: AuthCoordinator
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.oauth;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import application.controller.oauth.exception.AuthException;

import static java.lang.Thread.sleep;

import static application.controller.Config.AUTH_SERVER;
import static application.controller.Config.CLIENT_ID;
import static application.controller.Config.CLIENT_SECRET;
import static application.controller.Config.REDIRECT_URI;

/**
 * The <code>AuthCoordinator</code> class-"facade" coordinating the work of authorization through OAuth 2.0
 */
public class AuthCoordinator {

    private final String authLink;
    private RedirectServer server;
    private String accessToken;

    /**
     * Private constructor
     */
    private AuthCoordinator() {
        authLink = getAuthLink();
    }

    /**
     * Method for getting a new instance of a <code>AuthCoordinator</code> class
     * @return class instance
     */
    public static AuthCoordinator create() {
        return new AuthCoordinator();
    }

    /**
     * Returns a constructed string to query for the user code
     * @return query string with redirect
     */
    public String getAccessRequestLink() {
        return authLink;
    }

    /**
     * Get an access token obtained as a result of the exchange of code
     * @return               access token
     * @throws AuthException in cases of connection problems and inability to get the code and token
     */
    public String getAccessToken() throws AuthException {
        try {
            return tryToExchangeCodeForToken();
        } catch (IOException | InterruptedException e) {
            throw new AuthException(e.getMessage());
        }
    }

    /**
     * Attempts to exchange the grant received from the user for an application access token
     *
     * @return                      access token
     * @throws IOException          if there are exchange problems (see <code>RedirectServer</code> code for more info).
     * @throws InterruptedException if there are exchange problems (see <code>HttpClientCode</code> for more info).
     */
    private String tryToExchangeCodeForToken() throws IOException, InterruptedException {
        waitForAuthorizationCodeGrant();
        exchangeAuthorizationCodeForAccessToken();

        return accessToken;
    }

    /**
     * Groups and call the execution of methods to obtain authorization code,
     * which can then be exchanged for a access token in the future.
     * @throws IOException if there are exchange problems.
     */
    private void waitForAuthorizationCodeGrant() throws IOException {
        server = RedirectServer.create();
        server.startServer();
        waitingForCode();
        server.stopServer(1);
    }

    /**
     * The method exchanges the received grant authorization code for access token.
     *
     * @throws IOException          if there are exchange problems (see <code>RedirectServer</code> code for more info).
     * @throws InterruptedException if there are exchange problems (see <code>HttpClientCode</code> for more info).
     */
    private void exchangeAuthorizationCodeForAccessToken() throws IOException, InterruptedException {
        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        getAccessTokenRequestBody()
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getAuthUriString()))
                .build();

        HttpResponse<String> responseWithAccessToken = HttpClient
                .newBuilder()
                .build()
                .send(requestForAccessToken, HttpResponse.BodyHandlers.ofString());

        accessToken = parseAccessToken(responseWithAccessToken.body());
    }

    /**
     * Parses the token from the response string
     *
     * @param bearerToken source string
     * @return            access token
     */
    private static String parseAccessToken(final String bearerToken) {
        JsonObject object = JsonParser.parseString(bearerToken).getAsJsonObject();
        return object.get("access_token").getAsString();
    }

    /**
     * The method expects to receive a grant code authorization from <code>RedirectServer</code>
     */
    @SuppressWarnings("BusyWait")
    private void waitingForCode() {
        /*
            Not the most elegant solution, but since the application is not multithreaded and to
            access all the functionality, first need to get access, the solution is acceptable.
         */
        while (server.getCode() == null) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                assert false : "This will never happen";
            }
        }
    }

    /*
        Methods below are used to build authorization strings

        During the refactoring, all StringBuilders were replaced with simple string concatenation. If my
        solution raises questions, please see the article to understand my solution (till JDK1.4):
        https://medium.com/javarevisited/java-compiler-optimization-for-string-concatenation-7f5237e5e6ed
    */

    private static String getAuthUriString() {
        return AUTH_SERVER + "/api/token";
    }

    private String getAccessTokenRequestBody() {
        return "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=authorization_code" +
                "&code=" + server.getCode() +
                "&redirect_uri=" + REDIRECT_URI;
    }

    private static String getAuthLink() {
        return AUTH_SERVER +
               "/authorize?client_id=" + CLIENT_ID +
               "&redirect_uri=" + REDIRECT_URI +
               "&response_type=code";
    }
}