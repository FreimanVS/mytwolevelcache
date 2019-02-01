package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.businesslogic.services.Utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class HDDCacheLRU implements LRUCache {

    private final Map<String, String> paths = new LinkedHashMap<>();

    @Override
    public void add(final String name, final Data data) {
        final String prefix = "cache/";
        final String path = prefix + name;
        Utils.serialize(path, data);
        paths.put(name, path);
    }

    @Override
    public boolean contains(final String name) {
        return paths.containsKey(name);
    }

    @Override
    public Data get(final String name) {
        final String path = paths.get(name);
        final Data data = (Data)Utils.deserialize(path);
        if (Objects.nonNull(data))
            update(name, data);
        return data;
    }

    @Override
    public boolean remove(String name) {
        return paths.remove(name) != null;
    }

    @Override
    public Map.Entry removeLRU() {
        if (paths.isEmpty()) return null;

        Iterator<Map.Entry<String, String>> iterator = paths.entrySet().iterator();
        Map.Entry<String, String> entry = iterator.next();
        iterator.remove();
        return entry;
    }

    @Override
    public String toString() {
        return paths.toString();
    }

    @Override
    public int size() {
        return paths.size();
    }
}
