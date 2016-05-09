package com.veontomo.yoda;

import java.util.Map;

/**
 * Interface for caching objects.
 */
public interface Cache<T, K> {
    void put(final T t, final K k);
    T get(int pos);

    T getRandom();

    String[] serialize();
}
