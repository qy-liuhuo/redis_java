package org.qyliuhuo;

import org.junit.Test;

public class CacheBuilderTest {
    @Test
    public void testCacheBuilder(){
        Cache<String,String> cache = new CacheBuilder().setSize(10000).build();
        System.out.println(cache.size());
    }

}