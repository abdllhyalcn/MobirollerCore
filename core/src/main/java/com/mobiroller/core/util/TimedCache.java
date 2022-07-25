package com.mobiroller.core.util;

import android.os.SystemClock;
import android.util.LruCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimedCache<K, V> {
    private LruCache mLruCache;
    private long mExpiryTimeInMillis;
    private Map<K, Long> mTimeMap;

    /**
     * @param maxSize for caches that do not override sizeOf(K, V), this is the maximum number of
     * entries in the cache. For all other caches, this is the maximum sum of the
     * sizes of the entries in this cache.
     * @param expiryTimeInMillis the period after which the entries in the will be assumed
     * as have been expired and will not be returned.
     */
    public TimedCache(int maxSize, long expiryTimeInMillis) {
        mLruCache = new LruCache<K, V>(maxSize);
        mTimeMap = new ConcurrentHashMap<>();
        mExpiryTimeInMillis = expiryTimeInMillis;
    }

    private boolean isValidKey(K key) {
        return key != null && mTimeMap.containsKey(key);
    }

    public synchronized V get(K key) {
        return getIfNotExpired(key, System.currentTimeMillis() - mExpiryTimeInMillis);
    }

    public synchronized V getIfNotExpired(K key, long expiryTimeInMillis) {
        if (!isValidKey(key)) {
            return null;
        }
        if (SystemClock.elapsedRealtime() - mTimeMap.get(key) <= expiryTimeInMillis) {
            return (V) mLruCache.get(key);
        } else {
            remove(key);
            return null;
        }
    }

    public synchronized void put(K key, V value) {
        if (key != null && value != null) {
            mLruCache.put(key, value);
            mTimeMap.put(key, SystemClock.elapsedRealtime());
        }
    }

    public synchronized void remove(K key) {
        if (key != null) {
            mLruCache.remove(key);
            mTimeMap.remove(key);
        }
    }

    public synchronized void clear() {
        mLruCache.evictAll();
        mTimeMap.clear();
    }
}
