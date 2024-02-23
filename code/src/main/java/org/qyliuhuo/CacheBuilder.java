package org.qyliuhuo;

import java.util.HashMap;
import java.util.Map;

public class CacheBuilder<K,V> {
    private Map<K,V> map= new HashMap<>();
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
        cache.setSize(size);
        return cache;
    }
}
