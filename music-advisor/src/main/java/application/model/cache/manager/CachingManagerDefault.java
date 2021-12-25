/* Class name: CachingManagerDefault
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model.cache.manager;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;

import application.model.UserRequestType;

import application.model.cache.container.CachingWrapper;
import application.model.cache.exception.CacheExpiredException;

/**
 * Simplified implementation of object caching for data received from Spotify API.
 */
public class CachingManagerDefault implements Cache<UserRequestType, JsonObject> {

    private int secondsToExpiration;
    private final Map<UserRequestType, CachingWrapper<JsonObject>> cache;

    /**
     * Private constructor
     * @param secondsToExpiration cache freshness time
     */
    private CachingManagerDefault(int secondsToExpiration) {
        this.secondsToExpiration = secondsToExpiration;
        cache = new HashMap<>();
    }

    /**
     * Method for getting a new instance of a class
     *
     * @param secondsToExpiration seconds during which the cache will be fresh
     * @return                    the <code>CachingManagerDefault</code> new instance
     */
    public static CachingManagerDefault create(int secondsToExpiration) {
        return new CachingManagerDefault(secondsToExpiration);
    }

    /* Accessors */

    /**
     * Sets a new value for cache freshness
     * @param secondsToExpiration cache freshness time in seconds
     */
    public void setSecondsToExpiration(int secondsToExpiration) {
        this.secondsToExpiration = secondsToExpiration;
    }

    /**
     * Returns the set cache freshness value
     * @return cache freshness time in seconds
     */
    public int getSecondsToExpiration() {
        return secondsToExpiration;
    }

    /* Implemented Cache interface methods */
    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void put(UserRequestType key, JsonObject value) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(secondsToExpiration);
        cache.put(key, new CachingWrapper(value, expiration));
    }
}