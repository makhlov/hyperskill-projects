package application.model;

import application.model.cache.exception.CacheExpiredException;
import application.model.cache.manager.CachingManagerDefault;
import application.model.exception.ClientServerException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import application.model.cache.manager.Cache;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static application.model.UserRequestType.*;

public class ModelDefault {
    private String resource;
    private String accessToken;
    private Cache<UserRequestType, JsonObject> cache;

    private ModelDefault(String resource, String accessToken) {
        cache = CachingManagerDefault.create();
        this.resource = resource;
        this.accessToken = accessToken;
    }

    public static ModelDefault create(String resource, String accessToken) {
        return new ModelDefault(resource, accessToken);
    }

    public JsonObject get(UserRequestType type, String[] args) throws ClientServerException {
        return getFromCacheOrReload(type, args);
    }

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

    private String definePath(UserRequestType type, String[] args) {
        return switch (type) {
            case FEATURED_PLAYLISTS -> "/v1/browse/featured-playlists";
            case CATEGORIES -> "/v1/browse/categories";
            case NEW_RELEASES -> "/v1/browse/new-releases";
            case PLAYLISTS -> "/v1/browse/categories/" + args[0];
        };
    }

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