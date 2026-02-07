package com.example.spring_boot_mode.test;

import java.util.*;

public class Mapforeach {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("qw","12");
        map.put("we","23");
        map.put("er","34");
        //1. map.entrySet();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey()+"----"+entry.getValue());
        }
        //2.map.keySet()
        for (String s : map.keySet()) {
            String s1 = map.get(s);
            System.out.println(s+"---"+s1);
        }
        //3.map.foreach()
        map.forEach((k,v)->{
            System.out.println(k+"---"+v);
        });
        //4.map.entrySety().iterator
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            System.out.println(next.getKey()+"----"+next.getValue());
        }
        //5.map.values()
        System.out.println("5--------------");
        Collection<String> values = map.values();
        for (String value : values) {
            System.out.println("---"+value);
        }
        //5.map.entrySet().steam().forEach();
        map.entrySet().stream().filter(entry -> !entry.getKey().equals("qw")).sorted((o1, o2) -> o2.getValue().toString().compareTo(o1.getValue().toString())).forEach(entry -> System.out.println(entry.getKey() + "----" + entry.getValue()));
    }
}
