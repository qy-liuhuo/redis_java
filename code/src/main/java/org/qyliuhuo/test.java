package org.qyliuhuo;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        Map<String,String> test = new HashMap<>();
        for (int i=0;i<100;i++){
            test.put(String.valueOf(i),String.valueOf(i));
        }
        System.out.println(test.size());
    }

}
