package com.veontomo.yoda;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Quote cache
 */
public class QuoteCache implements Cache<Quote, String> {

    private static final String TAG = Config.appName;
    /**
     * the maximal number of the items that the cache might contain. It is supposed to be positive.
     */
    private final int maxSize;

    private final LinkedHashMap<Quote, String> items;

    /**
     * current mSize of the cache
     */
    private int mSize;

    /**
     * Constructor.
     *
     * @param maxSize the maximal number of the items that the cache might contain
     */
    public QuoteCache(int maxSize) {
        this.maxSize = maxSize;
        items = new LinkedHashMap<>();
        mSize = 0;
    }

    public int getSize() {
        return mSize;
    }

    @Override
    public void put(final Quote quote, final String str) {
        if (items.size() >= maxSize) {
            items.remove(0);
            mSize--;
        }
        items.put(quote, str);
        mSize++;
    }

    @Override
    public Quote get(int pos) {
        return (pos < mSize) ? new ArrayList<>(items.keySet()).get(pos) : null;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (mSize <= 0) {
            return null;
        }
        int pos = generator.nextInt(items.size());
        return get(pos);
    }

    @Override
    public String[] serialize() {
        String[] data = new String[mSize * 2];
        int i = 0;
        for (Map.Entry<Quote, String> entry : items.entrySet()) {
            data[i] = entry.getKey().serialize();
            data[i + 1] = entry.getValue();
            i = i + 2;
        }
        return data;
    }

    /**
     * Loads a new content into the cache.
     *
     * @param data
     */
    @Override
    public void load(final String[] data) {
        final int size = data.length;
        items.clear();
        mSize = 0;
        Quote key;
        String value;
        for (int i = 0; i < size; i = i + 2) {
            key = Quote.deserialize(data[i]);
            if (key != null) {
                value = data[i + 1];
                items.put(key, value);
                mSize++;
            }
        }
    }


}
