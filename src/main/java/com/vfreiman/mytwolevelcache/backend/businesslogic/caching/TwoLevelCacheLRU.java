package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.Map;
import java.util.Objects;

/**
 * TwoLevelCacheLRU принимает в конструкторе максимальный размер кэша.
 * Распределяет его между кэшем в RAM и кэшем на HDD примерно пополам по стратегии Least Recent Used.
 *
 * TwoLevelCacheLRU takes cache size as a constructor argument.
 * It distributes it between RAM cache and HDD cache about 50/50 by the Least Recent Used strategy.
 */
public class TwoLevelCacheLRU implements LRUCache {

    private final int cacheSize;
    private final LRUCache ramCache;
    private final LRUCache hddCache;
    private int size;

    public TwoLevelCacheLRU(int cacheSize) {
        if (cacheSize < 2) throw new IllegalArgumentException("cache size can not be less than 2");
        this.cacheSize = cacheSize;
        this.ramCache = new RAMCacheLRU();
        this.hddCache = new HDDCacheLRU();
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
        if (Objects.nonNull(entry))
            hddCache.add(entry.getKey(), entry.getValue());
    }

    @Override
    public boolean contains(String name) {
        return ramCache.contains(name) || hddCache.contains(name);
    }

    @Override
    public Data get(String name) {
        Data data = ramCache.get(name);
        if (Objects.nonNull(data)) {
            update(name, data);
            return data;
        }

        data = hddCache.get(name);
        if (Objects.nonNull(data)) {
            update(name, data);
        }

        return data;
    }

    @Override
    public boolean remove(String name) {
        if (ramCache.remove(name)) {
            size --;
            return true;
        }

        if (hddCache.remove(name)) {
            size --;
            replace();
            return true;
        }
        return false;
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
        return "TwoLevelCacheLRU{" +
                "ramCache=" + ramCache +
                ", hddCache=" + hddCache +
                '}';
    }
}
