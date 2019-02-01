package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.Map;
import java.util.Objects;

/**
 * TwoLevelCacheLRU принимает в конструкторе максимальный размер кэша.
 * Распределяет его между кэшем в RAM и кэшем на HDD примерно пополам по стратегии Least Recent Used,
 * пока не достигнет cacheSize. Потом оставляет HDD cache максимально заполенным LRE элементами.
 *
 * TwoLevelCacheLRU takes cache size as a constructor argument.
 * It distributes it between RAM cache and HDD cache about 50/50 by the Least Recent Used strategy,
 * until cacheSize is reached. Then it holds HDD cache filled with LRE elements.
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
            moveToHDDCache();
        } else {
            if (size >= cacheSize / 2)
                moveToHDDCache();

            size ++;
        }

        ramCache.add(name, data);
    }

    /**
     * Переместить LRU элемент из RAM кэша в HDD кэш.
     *
     * Move LRU item from RAM cache to HDD cache.
     */
    private void moveToHDDCache() {
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
            moveToHDDCache();
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
            moveToHDDCache();
        }
        size --;
        return entry;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "TwoLevelCacheLRU{" +
                "ramCache=" + ramCache +
                ", hddCache=" + hddCache +
                '}';
    }
}
