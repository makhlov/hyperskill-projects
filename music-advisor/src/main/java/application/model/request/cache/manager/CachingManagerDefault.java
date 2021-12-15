package application.model.request.cache.manager;

import application.model.request.UserRequestType;
import application.model.request.cache.container.CachingWrapper;
import application.model.request.cache.exception.CacheExpiredException;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static utils.PropertiesReader.readMainProperties;

public class CachingManagerDefault implements Cache<UserRequestType, JsonArray> {

    private int secondsToExpiration;

    private final Map<UserRequestType, CachingWrapper<JsonArray>> cache;

    private CachingManagerDefault() {
        this.secondsToExpiration = getDefaultExpirationSecondsFromConfig();
        cache = new HashMap<>();
    }

    public static CachingManagerDefault create() {
        return new CachingManagerDefault();
    }

    private int getDefaultExpirationSecondsFromConfig() {
        int seconds = 60;
        try {
            seconds = Integer.parseInt(readMainProperties().getProperty("cache.expirationSeconds"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seconds;
    }

    @Override
    public JsonArray get(UserRequestType key) throws CacheExpiredException {
        try {
            return cache.get(key).getIfNotExpired();
        } catch (CacheExpiredException e) {
            cache.remove(key);
            throw new CacheExpiredException(e.getMessage());
        }
    }

    @Override
    public void put(UserRequestType key, JsonArray value) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(secondsToExpiration);
        cache.put(key, new CachingWrapper(value, expiration));
    }

    public void setSecondsToExpiration(int secondsToExpiration) {
        this.secondsToExpiration = secondsToExpiration;
    }

    public int getSecondsToExpiration() {
        return secondsToExpiration;
    }
}