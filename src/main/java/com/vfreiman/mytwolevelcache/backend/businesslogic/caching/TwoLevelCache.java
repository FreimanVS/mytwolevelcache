package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.Map;

/**
 * TwoLevelCache принимает в конструкторе максимальный размер кэша.
 * Распределяет его между кэшем в RAM и кэшем на HDD примерно пополам по признаку Least Recent Used
 */
public class TwoLevelCache implements LRUCache {

    private final int cacheSize;
    private final LRUCache ramCache;
    private final LRUCache hddCache;
    private int size;

    public TwoLevelCache(int cacheSize) {
        if (cacheSize < 2) throw new IllegalArgumentException("cache size can not be less than 2");
        this.cacheSize = cacheSize;
        this.ramCache = new RAMCache();
        this.hddCache = new HDDCache();
    }

    @Override
    public void add(String name, Data data) {

        if (size == cacheSize){
            hddCache.removeLRU();
            replace();
        } else {
            if (size >= cacheSize / 2)
                replace();

            size ++;
        }

        ramCache.add(name, data);
    }

    private void replace() {
        Map.Entry<String, Data> entry = (Map.Entry<String, Data>) ramCache.removeLRU();
        hddCache.add(entry.getKey(), entry.getValue());
    }

    @Override
    public boolean contains(String name) {
        return ramCache.contains(name) || hddCache.contains(name);
    }

    @Override
    public Data get(String name) {
        Data data = ramCache.get(name);
        if (data != null) return data;

        return hddCache.get(name);
    }

    @Override
    public boolean remove(String name) {
        boolean removeRamCache = ramCache.remove(name);
        boolean removeHddCache = hddCache.remove(name);

        if (removeRamCache || removeHddCache) {
            if (removeHddCache)
                replace();

            size--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map.Entry removeLRU() {
        if (size == 0) return null;

        Map.Entry entry;
        if (size <= cacheSize / 2) {
            entry = ramCache.removeLRU();
        } else {
            entry = hddCache.removeLRU();
            replace();
        }
        size --;
        return entry;
    }

    @Override
    public String toString() {
        return "TwoLevelCache{" +
                "ramCache=" + ramCache +
                ", hddCache=" + hddCache +
                '}';
    }
}
