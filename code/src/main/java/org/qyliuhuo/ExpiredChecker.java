package org.qyliuhuo;

import java.util.Map;
import java.util.TimerTask;

public class ExpiredChecker<K,V> extends TimerTask {
    private Map<K,V> map;
    private Map<K,Long> expiredMap;

    private final int ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP = 20;

    public ExpiredChecker(Map<K,V> map, Map<K,Long> expiredMap){
        this.map = map;
        this.expiredMap = expiredMap;
    }

    public boolean isExpired(K key){
        return expiredMap.get(key) > System.currentTimeMillis();
    }
    @Override
    public void run(){
        System.out.println("check");
        int expiredCount;
        do {
            expiredCount = 0;
            for(int i=0;i<ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP;i++){
                K key = ((RHashMap<K, Long>)expiredMap).getRandomkey();
                if (key!=null && isExpired(key)){
                    map.remove(key);
                    expiredMap.remove(key);
                    expiredCount++;
                }
            }
        }while (expiredCount>ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP/4);
    }
}
