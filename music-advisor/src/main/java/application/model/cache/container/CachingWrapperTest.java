package application.model.cache.container;

import application.model.cache.exception.CacheExpiredException;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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