package caching;

import connections.OracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SingletonCache {
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
}
