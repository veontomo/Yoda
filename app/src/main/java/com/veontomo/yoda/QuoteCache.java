package com.veontomo.yoda;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Quote cache
 *
 */
public class QuoteCache implements Cache<Quote> {

    private static final String TAG = Config.appName;
    /**
     * the maximal number of the items that the cache might contain. It is supposed to be positive.
     */
    private final int maxSize;

    private final List<Quote> items;

    /**
     * current size of the cache
     */
    private int size;

    /**
     * Constructor.
     * @param maxSize the maximal number of the items that the cache might contain
     */
    public QuoteCache(int maxSize) {
        this.maxSize = maxSize;
        items = new ArrayList<>();
        size = 0;
    }


    @Override
    public void put(final Quote quote) {
        if (items.size() >= maxSize){
            items.remove(0);
            size--;
        }
        items.add(quote);
        size++;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (size <= 0){
            return null;
        }
        int pos = generator.nextInt(items.size());
        Log.i(TAG, "getRandomFromCache: returning quote n." + pos + " from cache of size " + items.size() );
        return items.get(pos);
    }
}
