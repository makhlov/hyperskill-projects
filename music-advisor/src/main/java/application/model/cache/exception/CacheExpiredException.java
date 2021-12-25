/* Class name: CacheExpiredException
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model.cache.exception;

import java.time.LocalDateTime;

/**
 * Exceptions thrown when trying to get cached data that has expired
 */
public class CacheExpiredException extends Exception {

    public CacheExpiredException() {
        super();
    }

    public CacheExpiredException(String message) {
        super(message);
    }

    public CacheExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheExpiredException(LocalDateTime date) {
        super("This cache has expired at " + date.toString());
    }
}
