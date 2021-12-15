package application.model.request.cache.container;

import application.model.request.cache.exception.CacheExpiredException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CachingWrapperTest {

    private static final int DELAY_SECONDS = 2;

    @Test
    void tryToGetNotExpiredObject() throws CacheExpiredException {
        String check = "check";
        CachingWrapper<String> wrapper = new CachingWrapper(check, LocalDateTime.now().plusSeconds(DELAY_SECONDS * 10));
        assertTrue(wrapper.getIfNotExpired().equalsIgnoreCase(check));
    }

    @Test
    void tryToGetExpiredObject() {
        String check = "check";
        CachingWrapper<String> wrapper = new CachingWrapper(check, LocalDateTime.now().plusSeconds(DELAY_SECONDS));
        assertThrows(CacheExpiredException.class, () -> {
            Thread.sleep(DELAY_SECONDS * 1000);
            wrapper.getIfNotExpired();
        });
    }
}