package com.veontomo.yoda;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for QuoteCache class
 */
public class QuoteCacheTest {

    @Test
    public void getElementsFromEmptyCache() {
        QuoteCache cache = new QuoteCache(2);
        assertNull(cache.getKey(0));
        assertNull(cache.getKey(1));
    }

    @Test
    public void getElementsFromNonEmptyCache() {
        QuoteCache cache = new QuoteCache(2);
        Quote q1 = new Quote();
        Quote q2 = new Quote();
        cache.put(q1, "");
        cache.put(q2, "");
        assertEquals(q1, cache.getKey(0));
        assertEquals(q2, cache.getKey(1));
    }

    @Test
    public void testGetSizeReturnsZeroForEmptyCache(){
        QuoteCache cache = new QuoteCache(5);
        assertEquals(0, cache.size());
    }

    @Test
    public void testGetSizeReturnsOneForSingleElementCache(){
        QuoteCache cache = new QuoteCache(5);
        cache.put(new Quote(), "");
        assertEquals(1, cache.size());
    }

    @Test
    public void testGetSizeReturnsThreeForThreeElementCacheWithMaxCapacityThree(){
        QuoteCache cache = new QuoteCache(3);
        cache.put(new Quote(), "");
        cache.put(new Quote(), "");
        cache.put(new Quote(), "");
        assertEquals(3, cache.size());
    }

    @Test
    public void testGetSizeReturns3WhenPutting5ElementsIntoCacheWithCapacity3(){
        QuoteCache cache = new QuoteCache(3);
        cache.put(new Quote(), "translation");
        cache.put(new Quote(), "translation");
        cache.put(new Quote(), "translation");
        cache.put(new Quote(), "translation");
        cache.put(new Quote(), "translation");
        assertEquals(3, cache.size());
    }


    @Test
    public void testGetRandomReturnsNullFromEmptyCache() {
        QuoteCache cache = new QuoteCache(10);
        assertNull(cache.getRandomKey());
    }

    @Test
    public void testGetRandomReturnsNotNullFromSingleElementCache() {
        QuoteCache cache = new QuoteCache(1);
        cache.put(new Quote(), "");
        assertNotNull(cache.getRandomKey());
    }

    @Test
    public void testGetRandomReturnsNotNullFromThreeElementCache() {
        QuoteCache cache = new QuoteCache(3);
        cache.put(new Quote(), "");
        cache.put(new Quote(), "");
        cache.put(new Quote(), "");
        assertNotNull(cache.getRandomKey());
    }

    @Test
    public void testGetRandomReturnsTheSameFromSingleElementCache() {
        QuoteCache cache = new QuoteCache(1);
        Quote q = new Quote();
        q.author = "A.Uthor";
        q.category = "favorite";
        q.quote = "Never mind";
        cache.put(q, "");
        Quote q2 = cache.getRandomKey();
        assertEquals("A.Uthor", q2.author);
        assertEquals("favorite", q2.category);
        assertEquals("Never mind", q2.quote);
    }


}