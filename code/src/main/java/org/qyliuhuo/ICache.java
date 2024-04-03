package org.qyliuhuo;

import java.util.Map;

public interface ICache<K,V> extends Map<K,V> {
    Boolean expire(final K key, final long timeInMills);

    Boolean expireAt(final K key, final long timeInMills);

    ICache<K,V> setSize(int size);
}
