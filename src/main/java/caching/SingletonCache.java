package caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SingletonCache {
    private static final int MAX_SIZE = 100;
    private Map<Long, Object> map;
    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    private static SingletonCache singletonCache = new SingletonCache();

    public static SingletonCache getInstance(){
        return singletonCache;
    }

    private SingletonCache(){
        map = new ConcurrentHashMap<>();
        readWriteLock = new ReentrantReadWriteLock(true);
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public void put(Long key, Object value) {
        writeLock.lock();

        if(map.size() == MAX_SIZE)
            map.clear();

        try {
            map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public Object get(Long key) {
        readLock.lock();

        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void remove(Long key) {
        writeLock.lock();

        try {
            map.remove(key);
        } finally {
            readLock.unlock();
        }
    }
}
