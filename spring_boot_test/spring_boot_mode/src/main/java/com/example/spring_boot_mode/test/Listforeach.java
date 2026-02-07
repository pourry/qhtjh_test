package com.example.spring_boot_mode.test;

import cn.hutool.core.text.csv.CsvUtil;

import java.util.*;

public class Listforeach {
    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map = null;
        map = new HashMap<>();
        map.put("name", "aaa");
        map.put("age", 25);
        list.add(map);
        map = new HashMap<>();
        map.put("name", "bbb");
        map.put("age", 26);
        list.add(map);
        list.add(map);
        map = new HashMap<>();
        map.put("name", "ccc");
        map.put("age", 27);
        list.add(map);
        //1.list.foreach
        list.forEach(ob->{
            System.out.println(ob.get("name")+"----"+ob.get("age"));
        });
        //2.list.stream
        list.stream().filter(x->Integer.valueOf(x.get("age").toString())>26)
                      .sorted((x,y)->{
                          return x.get("age").toString().compareTo(y.get("age").toString());
                      }).distinct().limit(2);
        //iterator
        Iterator<Map<String, Object>> iterator = list.iterator();
        while (iterator.hasNext()){
            Map<String, Object> next = iterator.next();
            System.out.println(next.get("age")+"-----"+next.get("name"));
        }
        
    }
}
