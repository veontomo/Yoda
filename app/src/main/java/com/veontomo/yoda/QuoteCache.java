package com.veontomo.yoda;

import android.os.Parcel;
import android.os.Parcelable;

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
     * current size of the cache
     */
    private int size;

    /**
     * Constructor.
     *
     * @param maxSize the maximal number of the items that the cache might contain
     */
    public QuoteCache(int maxSize) {
        this.maxSize = maxSize;
        items = new LinkedHashMap<>();
        size = 0;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void put(final Quote quote, final String str) {
        if (items.size() >= maxSize) {
            items.remove(0);
            size--;
        }
        items.put(quote, str);
        size++;
    }

    @Override
    public Quote get(int pos) {
        return (pos < size) ? new ArrayList<Quote>(items.keySet()).get(pos) : null;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (size <= 0) {
            return null;
        }
        int pos = generator.nextInt(items.size());
        return get(pos);
    }

    @Override
    public String[] serialize() {
        String[] data = new String[size * 2];
        int i = 0;
        for (Map.Entry<Quote, String> entry : items.entrySet()) {
            data[i] = entry.getKey().serialize();
            data[i + 1] = entry.getValue();
        }
        return data;
    }



}
