package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RAMCacheLRU implements LRUCache {

    private final Map<String, Data> cache = new LinkedHashMap<>();

    @Override
    public void add(String name, Data data) {
        cache.put(name, data);
    }

    @Override
    public boolean contains(String name) {
        return cache.containsKey(name);
    }

    @Override
    public Data get(String name) {
        Data data = cache.get(name);
        if (Objects.nonNull(data))
            update(name, data);
        return data;
    }

    @Override
    public boolean remove(String name) {
        return cache.remove(name) != null;
    }

    @Override
    public Map.Entry removeLRU() {
        if (cache.isEmpty()) return null;

        Iterator<Map.Entry<String, Data>> iterator = cache.entrySet().iterator();
        Map.Entry<String, Data> entry = iterator.next();
        iterator.remove();
        return entry;
    }

    @Override
    public String toString() {
        return cache.toString();
    }

    @Override
    public int size() {
        return cache.size();
    }
}
