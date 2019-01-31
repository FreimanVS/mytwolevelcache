package com.vfreiman.mytwolevelcache.backend.businesslogic;

import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.Cache;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.TwoLevelCache;
import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.database.Database;

public class Server {

    private final Cache twoLevelCache;

    public Server (int size) {
        twoLevelCache = new TwoLevelCache(size);
    }

    public Data get(final String name) {
        if (twoLevelCache.contains(name)) {

            System.out.printf("%s from cache.%n" +
                    "%s%n" +
                    "==============================%n" +
                    "", name, twoLevelCache);

            return twoLevelCache.get(name);
        } else {
            final Data obj = Database.getByName(name);
            twoLevelCache.add(name, obj);

            System.out.printf("%s not from cache.%n" +
                    "%s%n" +
                    "==============================%n" +
                    "", name, twoLevelCache);

            return obj;
        }
    }
}
