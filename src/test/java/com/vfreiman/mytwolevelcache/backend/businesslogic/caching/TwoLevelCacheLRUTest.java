package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwoLevelCacheLRUTest {

    private Cache cache;

    @Before
    public void before() {
        cache = new TwoLevelCacheLRU(4);
    }

    @Test
    public void add() {
        cache.add("a", new Data("a"));
        int expected = 1;
        int actual = cache.size();
        assertEquals(expected, actual);

        cache.add("b", new Data("b"));
        cache.add("c", new Data("c"));
        expected = 3;
        actual = cache.size();
        assertEquals(expected, actual);
    }

    @Test
    public void remove() {
        cache.add("a", new Data("a"));
        cache.add("b", new Data("b"));
        cache.add("c", new Data("c"));

        cache.remove("b");
        int expected = 2;
        int actual = cache.size();
        assertEquals(expected, actual);

        cache.remove("a");
        cache.remove("c");
        expected = 0;
        actual = cache.size();
        assertEquals(expected, actual);
    }

    @After
    public void after() {
        cache = null;
    }
}