package com.veontomo.yoda;

import android.os.Parcelable;

import java.util.Map;

/**
 * Interface for caching objects.
 */
public interface Cache<T extends Parcelable, K> {
    void put(final T t, final K k);
    T get(int pos);

    T getRandom();

    String[] serialize();

    void load(final String[] data);

    void loadBundle(Parcelable cache);
}
