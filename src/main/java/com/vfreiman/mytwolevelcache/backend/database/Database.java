package com.vfreiman.mytwolevelcache.backend.database;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.HashMap;

public class Database {

    private static final HashMap<String, Data> mySql = new HashMap<>();

    public static void add(String name, Data data) {
        mySql.put(name, data);
    }
    public static Data getByName(final String name) {
        return mySql.get(name);
    }
}
