/* Class name: Cache
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model.cache.manager;

import application.model.cache.exception.CacheExpiredException;

/**
 * Simplified cache abstraction
 *
 * @param <K> the key by which objects are retrieved from the cache and cached
 * @param <V> cached object
 */
public interface Cache<K, V> {

    /**
     *  Retrieves an object from the cache if the object has not expired, otherwise throws exceptions
     *
     * @param key                    the key by which the object is retrieved from the cache
     *
     * @return                       cached object
     * @throws CacheExpiredException if the object has expired
     */
    V get(final K key) throws CacheExpiredException;

    /**
     * Adds an object to the cache
     *
     * @param key   object key
     * @param value cached object
     */
    void put(final K key, final V value);
}