package com.vfreiman.mytwolevelcache;

import com.vfreiman.mytwolevelcache.backend.businesslogic.Server;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.Cache;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.HDDCacheLRU;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.RAMCacheLRU;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.TwoLevelCacheLRU;
import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.database.Database;
import com.vfreiman.mytwolevelcache.frontend.Client;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        fillDB();

        final int SIZE = 4;
        final Cache twoLevelCache = new TwoLevelCacheLRU(SIZE);
        final Cache hddCache = new HDDCacheLRU();
        final Cache ramCache = new RAMCacheLRU();

        clientRequest(twoLevelCache);
    }

    private static void clientRequest(final Cache cache) {
        new Client(new Server(cache)) {
            @Override
            public void request() {
                IntStream.range(0, 2).forEach(n -> {
                    server.get("a");
                    server.get("b");
                    server.get("c");
                    server.get("d");
                });
            }
        }.request();
    }

    private static void fillDB() {
        Database.add("a", new Data("content A"));
        Database.add("b", new Data("content B"));
        Database.add("c", new Data("content C"));
        Database.add("d", new Data("content D"));
        Database.add("e", new Data("content E"));
    }
}
