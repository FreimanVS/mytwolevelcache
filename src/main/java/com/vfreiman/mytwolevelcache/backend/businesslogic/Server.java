package com.vfreiman.mytwolevelcache.backend.businesslogic;

import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.Cache;
import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.database.Database;

public class Server {

    private final Cache cache;

    public Server(Cache cache) {
        this.cache = cache;
    }

    public Data get(final String name) {
        if (cache.contains(name)) {

            Data data = cache.get(name);

            System.out.printf("%s from cache.%n" +
                    "%s%n" +
                    "==============================%n" +
                    "", name, cache);

            return  data;
        } else {
            final Data obj = Database.getByName(name);
            cache.add(name, obj);

            System.out.printf("%s not from cache.%n" +
                    "%s%n" +
                    "==============================%n" +
                    "", name, cache);

            return obj;
        }
    }
}
