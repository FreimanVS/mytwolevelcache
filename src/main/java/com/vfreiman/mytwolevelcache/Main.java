package com.vfreiman.mytwolevelcache;

import com.vfreiman.mytwolevelcache.backend.businesslogic.Server;
import com.vfreiman.mytwolevelcache.backend.businesslogic.caching.*;
import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;
import com.vfreiman.mytwolevelcache.backend.database.Database;
import com.vfreiman.mytwolevelcache.frontend.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        fillDB();

        /**
         * one thread
         */
        final int SIZE = 4;
        final LRUCache twoLevelCache = new TwoLevelCacheLRU(SIZE);
        final LRUCache hddCache = new HDDCacheLRU();
        final LRUCache ramCache = new RAMCacheLRU();

        clientRequest(twoLevelCache);

        /**
         * concurrent
         */
        final LRUCache syncTwoLevelCache = Cachings.synchronizedLRUCache(twoLevelCache);
        final LRUCache syncHddCache = Cachings.synchronizedLRUCache(hddCache);
        final LRUCache syncRamCache = Cachings.synchronizedLRUCache(ramCache);

//        concurrentClientRequests(twoLevelCache, 300); //works with exceptions in concurrent
        concurrentClientRequests(syncTwoLevelCache, 300); //for concurrent use
    }

    private static void concurrentClientRequests(final Cache cache, final int threads) {
        final ExecutorService es = Executors.newFixedThreadPool(threads);
        final List<CompletableFuture<Void>> futures = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        IntStream.range(0, threads).forEach((n) -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                clientRequest(cache);
            }, es);
            futures.add(future);
        });

        latch.countDown();
        futures.forEach(CompletableFuture::join);
        es.shutdown();
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
