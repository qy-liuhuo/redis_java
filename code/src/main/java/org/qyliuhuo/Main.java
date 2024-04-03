package org.qyliuhuo;

public class Main {
    public static void main(String[] args) {
        Cache<String,String> cache = new CacheBuilder<String,String>().setSize(10000).build();
        cache.put("kj","yyds");
        System.out.printf(cache.get("kj"));
        while (true){

        }

    }
}