package com.veontomo.yoda;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Quote class tests
 *
 */
public class QuoteTest {

    @Test
    public void serializeEmpty() {
        Quote q1 = new Quote();
        String qSer = q1.serialize();
        Quote q2 = Quote.deserialize(qSer);
        assertEquals("author must be an empty string", "", q2.author);
        assertEquals("quote content must be an empty string", "", q2.quote);
        assertEquals("category must be an empty string", "", q2.category);
    }

    @Test
    public void serializeNonEmpty() {
        Quote q1 = new Quote();
        q1.quote = "quote content";
        q1.author = "somebody";
        q1.category = "news";
        String qSer = q1.serialize();
        Quote q2 = Quote.deserialize(qSer);
        assertEquals("author must be \"somebody\"", "somebody", q2.author);
        assertEquals("quote content must be \"quote content\"", "quote content", q2.quote);
        assertEquals("category must be \"news\"", "news", q2.category);
    }

}