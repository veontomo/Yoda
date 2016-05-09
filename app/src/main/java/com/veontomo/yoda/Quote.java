package com.veontomo.yoda;

/**
 * Quote how it is retrieved from quote-related API
 */
public class Quote {
    public static final String SEPARATOR = "#";
    public String quote = "";
    public String author = "";
    public String category = "";

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
}
