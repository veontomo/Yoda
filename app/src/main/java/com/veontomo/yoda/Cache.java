package com.veontomo.yoda;

import android.os.Parcelable;

/**
 * Interface for caching objects.
 */
interface Cache<T extends Parcelable, K> {
    void put(final T t, final K k);

    T getKey(int pos);

    /**
     * Returns a random key from key-value pairs stored in the cache.
     * @return a random quote
     */
    T getRandomKey();
}
