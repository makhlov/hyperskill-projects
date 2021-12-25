/* Class name: CachingWrapper
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model.cache.container;

import application.model.cache.exception.CacheExpiredException;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

/**
 * Wrapper class for simplified object caching
 *
 * @param <T> the object to be cached
 */
public class CachingWrapper<T> {
    private final T object;
    private final LocalDateTime expiresDate;

    /**
     * Wraps an object with expiration time
     *
     * @param object wrapped object
     */
    public CachingWrapper(final T object, LocalDateTime date) {
        this.expiresDate = date;
        this.object = object;
    }

    /**
     * Get wrapped object expiration date
     *
     * @return expiration date
     */
    public LocalDateTime getExpiresDate() {
        return expiresDate;
    }

    /**
     * Returns the wrapped object if it is actual
     *
     * @return wrapped object
     * @throws CacheExpiredException if the object has expired
     */
    public T getIfNotExpired() throws CacheExpiredException {
        if (now().isAfter(expiresDate)) {
            throw new CacheExpiredException(expiresDate);
        }
        return object;
    }

    /**
     * Returns an object even if it has expired
     *
     * @return wrapped object
     */
    public T getEvenIfExpired() {
        return object;
    }
}