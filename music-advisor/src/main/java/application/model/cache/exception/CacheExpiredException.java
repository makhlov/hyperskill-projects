package application.model.cache.exception;

import java.time.LocalDateTime;

public class CacheExpiredException extends Exception {

    public CacheExpiredException(LocalDateTime date) {
        super("This cache has expired at " + date.toString());
    }

    public CacheExpiredException(String message) {
        super(message);
    }
}
