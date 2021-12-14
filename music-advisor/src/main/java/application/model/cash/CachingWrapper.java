package application.model.cash;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

/**
 * Wrapper class for simplified object caching received from Spotify API
 *
 * @param <T> the object to be cached
 */
public class CachingWrapper<T> {
    private T object;
    private LocalDateTime expiresDate;

    private CachingWrapper(T object) {
        expiresDate = now().plusMinutes(3);
        this.object = object;
    }

    /**
     * Wraps an object
     *
     * @param object wrapped object
     * @return object wrapper
     */
    public CachingWrapper wrap(T object) {
        return new CachingWrapper(object);
    }

    /**
     * Returns the wrapped object if it is actual
     *
     * @return wrapped object
     * @throws СacheExpiredException if the object has expired
     */
    public T getObject() throws СacheExpiredException {
        if (now().isAfter(expiresDate)) {
            throw new СacheExpiredException(expiresDate);
        }
        return object;
    }
}