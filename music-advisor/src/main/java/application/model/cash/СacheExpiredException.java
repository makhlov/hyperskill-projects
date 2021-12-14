package application.model.cash;

import java.time.LocalDateTime;

public class СacheExpiredException extends Exception {

    public СacheExpiredException(LocalDateTime date) {
        super("This cache has expired at " + date.toString());
    }
}
