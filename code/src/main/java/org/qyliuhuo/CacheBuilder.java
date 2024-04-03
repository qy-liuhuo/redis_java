package org.qyliuhuo;

import java.util.Map;
import java.util.Timer;

public class CacheBuilder<K,V> {
    private Map<K,V> map= new RHashMap<>();

    private Map<K,Long> expiredMap = new RHashMap<>();
    private int size = Integer.MAX_VALUE;
    public CacheBuilder(){
    }

    public CacheBuilder<K,V> setSize(int size){
        if (size<=0){
            throw new IllegalArgumentException("param 'size' must be greater than 0'");
        }
        this.size=size;
        return this;
    }
    public Cache<K,V> build(){
        Cache<K,V> cache = new Cache<>();
        cache.map(map);
        cache.expiredMap(expiredMap);
        cache.setSize(size);
        Timer timer = new Timer(true);
        ExpiredChecker<K, V> expiredChecker = new ExpiredChecker<>(map,expiredMap);
        timer.schedule(expiredChecker,10*1000,10*1000);
        return cache;
    }
}
