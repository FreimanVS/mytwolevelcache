package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.io.Serializable;

public interface Cache extends Serializable {
    void add(String name, Data data);
    boolean contains(String name);
    Data get(String name);
    boolean remove(String name);
    int size();
}
