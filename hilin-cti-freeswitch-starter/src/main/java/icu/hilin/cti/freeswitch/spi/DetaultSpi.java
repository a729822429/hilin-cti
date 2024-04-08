package icu.hilin.cti.freeswitch.spi;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.LRUCache;

public class DetaultSpi implements ICacheSpi {

    private static final Cache<String, String> DEFAULT_CACHE = new LRUCache<>(100000);

    public static final DetaultSpi INSTANCE = new DetaultSpi();

    private DetaultSpi() {
    }

    @Override
    public void put(String key, String value) {
        DEFAULT_CACHE.put(key, value);
    }

    @Override
    public void put(String key, String value, long timeoutSeconds) {
        DEFAULT_CACHE.put(key, value, timeoutSeconds * 1000);
    }

    @Override
    public String get(String key) {
        return DEFAULT_CACHE.get(key);
    }

    @Override
    public void del(String key) {
        DEFAULT_CACHE.remove(key);
    }
}
