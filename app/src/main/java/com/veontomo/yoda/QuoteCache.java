package com.veontomo.yoda;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
     * current size of the cache
     */
    private int mSize;

    /**
     * Constructor.
     *
     * @param maxSize the maximal number of the items that the cache might contain
     */
    public QuoteCache(int maxSize) {
        this.maxSize = maxSize;
        this.mItems = new LinkedHashMap<>();
        this.mSize = 0;
    }

    protected QuoteCache(Parcel in) {
        this.maxSize = in.readInt();
        this.mSize = in.readInt();
        this.mItems = new LinkedHashMap<>();
        Quote q;
        String s;
        for (int i = 0; i < mSize; i++) {
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

    public int getSize() {
        return mSize;
    }

    @Override
    public void put(final Quote quote, final String str) {
        if (mItems.size() >= maxSize) {
            mItems.remove(0);
            mSize--;
        }
        mItems.put(quote, str);
        mSize++;
    }

    @Override
    public Quote getKey(int pos) {
        return (pos < mSize) ? new ArrayList<>(mItems.keySet()).get(pos) : null;
    }

    @Override
    public String getValue(int pos) {
        return (pos < mSize) ? new ArrayList<>(mItems.values()).get(pos) : null;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (mSize <= 0) {
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
     * If the given cache contains more elements than the current one might contain, then extra
     * elements are to be ignored.
     * @param cache
     */
    @Override
    public void loadBundle(Parcelable cache) {
        final QuoteCache c = (QuoteCache) cache;
        if (c != null) {
            int cSize = c.getSize();
            if (cSize > maxSize) {
                cSize = maxSize;
            }
            mItems.clear();
            for (int i = 0; i < cSize; i++) {
                mItems.put(c.getKey(i), c.getValue(i));
            }
            mSize = cSize;
        }
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
        dest.writeInt(mSize);
        for (Quote q : mItems.keySet()) {
            dest.writeParcelable(q, 0);
            dest.writeString(mItems.get(q));
        }
    }
}
