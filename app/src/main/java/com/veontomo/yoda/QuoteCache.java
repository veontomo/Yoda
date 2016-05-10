package com.veontomo.yoda;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
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
        this.mItems = new LinkedHashMap<>();
        this.mSize = 0;
    }

    protected QuoteCache(Parcel in) {
        Log.i(TAG, "QuoteCache: initally, parcel contains " + in.dataSize() );
        this.maxSize = in.readInt();
        this.mSize = in.readInt();
        this.mItems = new LinkedHashMap<>();
        Log.i(TAG, "QuoteCache: parcel contains " + in.dataSize() );
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
    public Quote get(int pos) {
        return (pos < mSize) ? new ArrayList<>(mItems.keySet()).get(pos) : null;
    }

    @Override
    public Quote getRandom() {
        Random generator = new Random();
        if (mSize <= 0) {
            return null;
        }
        int pos = generator.nextInt(mItems.size());
        return get(pos);
    }

    @Override
    public String[] serialize() {
        String[] data = new String[mSize * 2];
        int i = 0;
        for (Map.Entry<Quote, String> entry : mItems.entrySet()) {
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
        mItems.clear();
        mSize = 0;
        Quote key;
        String value;
        for (int i = 0; i < size; i = i + 2) {
            key = Quote.deserialize(data[i]);
            if (key != null) {
                value = data[i + 1];
                mItems.put(key, value);
                mSize++;
            }
        }
    }

    @Override
    public void loadBundle(Parcelable cache) {
        Log.i(TAG, "loadBundle: parcelable cache");
        final QuoteCache c = (QuoteCache) cache;
        if (c != null){
            mItems.clear();
            // TODO
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
