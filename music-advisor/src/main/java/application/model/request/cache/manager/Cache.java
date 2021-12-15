package application.model.request.cache.manager;

import application.model.request.cache.exception.CacheExpiredException;

public interface Cache<K, V> {
    V get(final K key) throws CacheExpiredException;
    void put(final K key, final V value);
}