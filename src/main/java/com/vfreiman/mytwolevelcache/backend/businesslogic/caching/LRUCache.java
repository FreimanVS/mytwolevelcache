package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.Map;

public interface LRUCache extends Cache {
    Map.Entry removeLRU();

    default void update(String name, Data data) {
        remove(name);
        add(name, data);
    }
}
