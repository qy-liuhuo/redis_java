package org.qyliuhuo;

import java.util.Map;

public interface ICache<K,V> extends Map<K,V> {
    ICache<K, V> expire(final K key, final long timeInMills);

    ICache<K, V> expireAt(final K key, final long timeInMills);

    ICache<K,V> setSize(int size);
}
