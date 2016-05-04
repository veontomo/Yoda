package com.veontomo.yoda;

/**
 * Interface for caching objects.
 */
public interface Cache<T> {
    void put(final T t);

    T getRandom();
}
