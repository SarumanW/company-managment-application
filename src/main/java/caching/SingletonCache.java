package caching;

import connections.OracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonCache {
    private Map<Long, Object> map;

    private static SingletonCache singletonCache = new SingletonCache();

    public static SingletonCache getInstance(){
        return singletonCache;
    }

    private SingletonCache(){
        map = new ConcurrentHashMap<Long, Object>();
    }

    public void put(Long key, Object value) {
        map.put(key, value);
    }

    public Object get(Long key) {
        Object object = map.get(key);
        return object;
    }
}
