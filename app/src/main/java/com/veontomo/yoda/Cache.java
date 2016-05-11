package com.veontomo.yoda;

import android.os.Parcelable;

import java.util.Map;

/**
 * Interface for caching objects.
 */
public interface Cache<T extends Parcelable, K> {
    void put(final T t, final K k);
    T getKey(int pos);
    K getValue(int pos);

    T getRandom();

    void loadBundle(Parcelable cache);
}
