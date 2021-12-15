package application.model.cache.manager;

import application.model.UserRequestType;
import application.model.cache.container.CachingWrapper;
import application.model.cache.exception.CacheExpiredException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static utils.PropertiesReader.readMainProperties;

public class CachingManagerDefault implements Cache<UserRequestType, JsonObject> {

    private int secondsToExpiration;

    private final Map<UserRequestType, CachingWrapper<JsonObject>> cache;

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
    public JsonObject get(UserRequestType key) throws CacheExpiredException, NullPointerException {
        try {
            return cache.get(key).getIfNotExpired();
        } catch (CacheExpiredException ce) {
            cache.remove(key);
            throw new CacheExpiredException(ce.getMessage());
        } catch (NullPointerException npe) {
            throw new NullPointerException(npe.getMessage());
        }
    }

    @Override
    public void put(UserRequestType key, JsonObject value) {
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