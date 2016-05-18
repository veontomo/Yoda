package com.veontomo.yoda;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Quote cache
 */
public class QuoteCache implements Cache<Quote, String>, Parcelable {
    /**
     * the maximal number of the items that the cache might contain. It is supposed to be positive.
     */
    private final int maxSize;

    /**
     * list that stores the key-value pairs
     */
    private final LinkedHashMap<Quote, String> mItems;

    /**
     * Constructor.
     *
     * @param maxSize the maximal number of the items that the cache might contain
     */
    public QuoteCache(int maxSize) {
        this.maxSize = maxSize;
        this.mItems = new LinkedHashMap<>();
    }

    private QuoteCache(Parcel in) {
        this.maxSize = in.readInt();
        this.mItems = new LinkedHashMap<>();
        Quote q;
        String s;
        final int size = size();
        for (int i = 0; i < size; i++) {
            q = in.readParcelable(null);
            s = in.readString();
            this.mItems.put(q, s);
        }
    }

    public static final Creator<QuoteCache> CREATOR = new Creator<QuoteCache>() {
        @Override
        public QuoteCache createFromParcel(Parcel in) {
            return new QuoteCache(in);
        }

        @Override
        public QuoteCache[] newArray(int size) {
            return new QuoteCache[size];
        }
    };

    /**
     * The number of elements the cache contains.
     *
     * @return cache size
     */
    public int size() {
        return mItems.size();
    }

    /**
     * Put the quote and its translation into this cache.
     * <p/>
     * If the quote is already present in the cache, it is not added.
     * <p/>
     * If the cache becomes larger that its maximally allowed capacity, then its first element is removed.
     *
     * @param quote
     * @param str
     */
    @Override
    public void put(final Quote quote, final String str) {
        if (mItems.containsKey(quote)) {
            return;
        }
        if (mItems.size() >= maxSize) {
            final Quote q = getKey(0);
            if (q != null) {
                mItems.remove(q);
            }
        }
        mItems.put(quote, str);
    }

    /**
     * Returns the key of a key-value map at given position.
     * <p/>
     * If a map at given position does not exist, null is returned
     *
     * @param pos
     * @return the quote at given position or null
     */
    @Override
    @Nullable
    public Quote getKey(int pos) {
        Iterator iterator = mItems.keySet().iterator();
        int pointer = 0;
        while (pointer < pos && iterator.hasNext()) {
            iterator.next();
            pointer++;
        }
        if (pointer == pos && iterator.hasNext()) {
            return (Quote) iterator.next();
        }
        return null;
    }

    /**
     * Returns a random key from key-value pairs stored in the cache.
     * @return a random quote
     */
    @Override
    public Quote getRandomKey() {
        Random generator = new Random();
        if (size() == 0) {
            return null;
        }
        int pos = generator.nextInt(mItems.size());
        return getKey(pos);
    }

    /**
     * Whether the cache contains given key.
     * @param key a key to find in the cache
     * @return true if the cache contains given key, false otherwise.
     */
    public boolean contains(final Quote key) {
        return mItems.containsKey(key);
    }

    /**
     * Returns a string associated with given quote
     *
     * @param key
     * @return value corresponding to the key
     */
    public String getValue(Quote key) {
        return mItems.get(key);
    }


    /**
     * Loads the items stored in given cache into {@link #mItems}.
     * <p/>
     * NB: no control is preformed whether the cache to be loaded contains too many elements.
     *
     * @param cache
     */
    public void loadBundle(Parcelable cache) {
        final QuoteCache c = (QuoteCache) cache;
        if (c != null) {
            mItems.clear();
            mItems.putAll(c.items());
        }
    }

    /**
     * {@link #mItems} getter
     * @return the cache key-value pairs
     */
    private Map<Quote, String> items() {
        return mItems;
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshaled representation.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxSize);
        for (Quote q : mItems.keySet()) {
            dest.writeParcelable(q, 0);
            dest.writeString(mItems.get(q));
        }
    }
}
