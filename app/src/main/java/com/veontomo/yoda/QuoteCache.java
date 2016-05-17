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
public class QuoteCache implements Cache<Quote, String>, Parcelable {

    private static final String TAG = Config.appName;
    /**
     * the maximal number of the items that the cache might contain. It is supposed to be positive.
     */
    private final int maxSize;

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

    protected QuoteCache(Parcel in) {
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
     * @return cache size
     */
    public int size() {
        return mItems.size();
    }

    /**
     * Put the quote and its translation into this cache.
     *
     * If the quote is already present in the cache, it is not added.
     *
     * If the cache becomes larger that its maximally allowed capacity, then its first element is removed.
     * @param quote
     * @param str
     */
    @Override
    public void put(final Quote quote, final String str) {
        if (mItems.containsKey(quote)){
            return;
        }
        if (mItems.size() >= maxSize) {
            mItems.remove(0);
        }
        mItems.put(quote, str);
}

    @Override
    public Quote getKey(int pos) {
        return (pos < size()) ? new ArrayList<>(mItems.keySet()).get(pos) : null;
    }

    @Override
    public String getValue(int pos) {
        return (pos < size()) ? new ArrayList<>(mItems.values()).get(pos) : null;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (size() == 0) {
            return null;
        }
        int pos = generator.nextInt(mItems.size());
        return getKey(pos);
    }

    @Override
    public boolean contains(final Quote key) {
        return mItems.containsKey(key);
    }

    /**
     * Returns cached value
     *
     * @param key
     * @return value corresponding to the key
     */
    @Override
    public String get(Quote key) {
        return mItems.get(key);
    }


    /**
     * Loads the items stored in given cache into {@link #mItems}.
     *
     * NB: no control is preformed whether the cache to be loaded contains too many elements.
     * @param cache
     */
    @Override
    public void loadBundle(Parcelable cache) {
        final QuoteCache c = (QuoteCache) cache;
        if (c != null) {
            mItems.clear();
            mItems.putAll(c.items());
        }
    }

    @Override
    public Map<Quote, String> items() {
        return mItems;
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
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
