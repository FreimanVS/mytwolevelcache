package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import com.vfreiman.mytwolevelcache.backend.businesslogic.entities.Data;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cachings {
    public static Cache synchronizedCache(Cache cache) {
        return new Cache() {

            private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock(false);
            private final ReentrantReadWriteLock.WriteLock writeLock = LOCK.writeLock();
            private final ReentrantReadWriteLock.ReadLock readLock = LOCK.readLock();

            @Override
            public void add(String name, Data data) {
                try {
                    writeLock.lock();
                    cache.add(name, data);
                } finally {
                    writeLock.unlock();
                }
            }

            @Override
            public boolean contains(String name) {
                try {
                    readLock.lock();
                    return cache.contains(name);
                } finally {
                    readLock.unlock();
                }
            }

            @Override
            public Data get(String name) {
                try {
                    writeLock.lock();
                    return cache.get(name);
                } finally {
                    writeLock.unlock();
                }
            }

            @Override
            public boolean remove(String name) {
                try {
                    writeLock.lock();
                    return cache.remove(name);
                } finally {
                    writeLock.unlock();
                }
            }

            @Override
            public int size() {
                try {
                    readLock.lock();
                    return cache.size();
                } finally {
                    readLock.unlock();
                }
            }
        };
    }
}
