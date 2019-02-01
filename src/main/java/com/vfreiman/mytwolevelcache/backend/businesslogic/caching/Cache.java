package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

public interface Cache {
    void add(String name, Data data);
    boolean contains(String name);
    Data get(String name);
    boolean remove(String name);
    int size();
}
