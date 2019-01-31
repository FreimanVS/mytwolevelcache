package com.vfreiman.mytwolevelcache.backend.businesslogic.caching;

import java.util.Map;

public interface LRUCache extends Cache {
    Map.Entry removeLRU();
}
