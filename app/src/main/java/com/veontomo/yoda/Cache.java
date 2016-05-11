package com.veontomo.yoda;

import android.os.Parcelable;

/**
 * Interface for caching objects.
 */
public interface Cache<T extends Parcelable, K> {
    void put(final T t, final K k);

    T getKey(int pos);

    K getValue(int pos);

    T getRandom();

    /**
     * Whether the cache contains given key.
     * @param key a key to find in the cache
     * @return true if the cache contains given key, false otherwise.
     */
    boolean contains(final T key);

    /**
     * Returns cached value
     * @param key
     * @return value corresponding to the key
     */
    K get(final T key);

    void loadBundle(Parcelable cache);
}
