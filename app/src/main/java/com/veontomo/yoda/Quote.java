package com.veontomo.yoda;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Quote how it is retrieved from quote-related API
 */
public class Quote implements Parcelable{
    public static final String SEPARATOR = "#";
    public String quote;
    public String author;
    public String category;

    protected Quote(Parcel in) {
        quote = in.readString();
        author = in.readString();
        category = in.readString();
    }

    public Quote(){
        quote = "";
        author = "";
        category = "";
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String serialize() {
        return quote + SEPARATOR + author + SEPARATOR + category;
    }

    public static Quote deserialize(final String str) {
        Quote q = new Quote();
        if (str == null) {
            return q;
        }
        String[] parts = str.split(SEPARATOR, -2);
        if (parts.length == 3) {
            q.quote = parts[0];
            q.author = parts[1];
            q.category = parts[2];
        }
        return q;
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
        dest.writeString(quote);
        dest.writeString(author);
        dest.writeString(category);
    }
}
