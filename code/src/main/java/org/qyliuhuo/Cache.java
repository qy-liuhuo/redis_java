package org.qyliuhuo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Cache<K,V> implements ICache<K,V> {
    /**
     * 存储类
     */
    private Map<K,V> map;

    private Map<K,Long> expiredMap;
    public void map(Map<K,V> map){
        this.map=map;
    }
    public void expiredMap(Map<K,Long> expiredMap){
        this.expiredMap=expiredMap;
    }

    /**
     * 设置过期时间间隔
     * @param key
     * @param timeInMills 过期时间间隔
     * @return
     */
    @Override
    public Boolean expire(K key, long timeInMills) {
        if(map.containsKey(key)){
            expiredMap.put(key,System.currentTimeMillis()+timeInMills);
            return true;
        }
        return false;
    }

    /**
     * 设置过期时刻
     * @param key 过期key
     * @param timeInMills 过期时刻
     * @return
     */
    @Override
    public Boolean expireAt(K key, long timeInMills) {
        if(map.containsKey(key)){
            expiredMap.put(key,timeInMills);
            return true;
        }
        return false;
    }

    @Override
    public ICache<K, V> setSize(int size) {
        return this;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        if(isExpired(key)){
            remove(key); // 删除key
            expiredMap.remove(key); //删除过期时间map
            return null;
        }
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (isExpired(key)){
            expiredMap.remove(key);
        }
        return map.put(key,value);
    }

    public Boolean isExpired(Object key){
        Long expiredTime =  expiredMap.get(key);
        if(expiredTime!=null && expiredTime<System.currentTimeMillis()) {
            return true;
        }
        return false;
    }
    @Override
    public V remove(Object key) {
        expiredMap.remove(key);
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
