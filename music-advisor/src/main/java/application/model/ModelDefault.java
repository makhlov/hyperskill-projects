/* Class name: ModelDefault
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import application.model.exception.ClientServerException;

import application.model.cache.exception.CacheExpiredException;
import application.model.cache.manager.CachingManagerDefault;
import application.model.cache.manager.Cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static application.model.UserRequestType.PLAYLISTS;

/**
 * The <code>ModelDefault</code> is a default implementation of the model meets the requirements
 * described in the task at hyperskill. The class executes requests to the API based on user input
 * and uses a simple implementation of request caching.
 */
public class ModelDefault implements Model {

    private final String resource;
    private final String accessToken;
    private final Cache<UserRequestType, JsonObject> cache;

    /**
     * Private constructor
     *
     * @param resource           api-resource from which the data will be received
     * @param accessToken        token for accessing the set <code>resource</code>
     * @param cacheExpirySeconds freshness time of the request cache in seconds
     */
    private ModelDefault(String resource, String accessToken, int cacheExpirySeconds) {
        cache = CachingManagerDefault.create(cacheExpirySeconds);
        this.resource = resource;
        this.accessToken = accessToken;
    }

    /**
     * Method for creating new class instances
     *
     * @param resource           api-resource from which the data will be received
     * @param accessToken        token for accessing the set <code>resource</code>
     * @param cacheExpirySeconds freshness time of the request cache in seconds
     *
     * @return                   a new instance of the class
     */
    public static ModelDefault create(String resource, String accessToken, int cacheExpirySeconds) {
        return new ModelDefault(resource, accessToken, cacheExpirySeconds);
    }

    /** {@inheritDoc} */
    @Override
    public JsonObject get(UserRequestType type, String[] args) throws ClientServerException {
        return getFromCacheOrReload(type, args);
    }

    /**
     * Receives the requested data from the cache, if the cache
     * has not expired or reloads the data from Spotify api.
     * If, for one reason or another, the data cannot be
     * loaded, the method throws an exception
     *
     * @param type                   the command that user is requesting to execute
     * @param args                   arguments passed along with the command
     *
     * @return                       the requested data is in the JsonObject form
     *
     * @throws ClientServerException exception problem loading data from the api
     */
    private JsonObject getFromCacheOrReload(UserRequestType type, String[] args) throws ClientServerException {
        String path = definePath(type, args);
        if (type == PLAYLISTS) {
            return loadData(path);
        }

        JsonObject result;
        try {
            result = cache.get(type);
        } catch (CacheExpiredException | NullPointerException e) {
            result = loadData(path);
            cache.put(type, result);
        }
        return result;
    }

    /**
     * Defines API paths for loading data
     *
     * @param type the command that user is requesting to execute
     * @param args arguments passed along with the command
     *
     * @return     path part for API URI
     */
    private String definePath(UserRequestType type, String[] args) {
        return switch (type) {
            case FEATURED_PLAYLISTS -> "/v1/browse/featured-playlists";
            case CATEGORIES -> "/v1/browse/categories";
            case NEW_RELEASES -> "/v1/browse/new-releases";
            case PLAYLISTS -> "/v1/browse/categories/" + args[0] + "/playlists";
        };
    }

    /**
     * Downloads data from api at the specified path
     *
     * @param path                   path to a specific handle
     * @return                       data loaded from API in JsonObject form
     * @throws ClientServerException if it is impossible to connect to the API or server side problems
     */
    private JsonObject loadData(String path) throws ClientServerException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", " Bearer " + accessToken)
                .uri(URI.create(resource + path))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            try {
                return JsonParser.parseString(response.body()).getAsJsonObject();
            } catch (NullPointerException npe) {
                String message = JsonParser.parseString(response.body())
                        .getAsJsonObject()
                        .get("error")
                        .getAsJsonObject()
                        .get("message").toString();
                throw new ClientServerException(message);
            }
        } catch (IOException | InterruptedException e) {
            throw new ClientServerException(e.getMessage());
        }
    }
}