package com.example.spring_boot_mode.test.design.factoryPatternDemo;

public interface Shape {
     String str = null;
    public void todo();
    public static void staticAlet(){
        System.out.println("Shape接口的静态方法");
    }
   default  void defautAlert(){
        System.out.println("Shape接口的default方法");
    }
}
