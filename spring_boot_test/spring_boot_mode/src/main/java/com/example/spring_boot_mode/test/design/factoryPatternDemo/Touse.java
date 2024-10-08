package com.example.spring_boot_mode.test.design.factoryPatternDemo;


public class Touse {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Shape shape = new ShapeImp1();
        shape.todo();
        Shape shape2 = new ShapeImp2();
        shape2.todo();
        tobuildFactory("ss");

    }
/**
 *     JDK自带的SPI特性实现了自动加载接口的实现类。
 */
    public static String tobuildFactory(String clas) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> aClass =  classLoader.loadClass("com.example.spring_boot_mode.test.design.factoryPatternDemo.ShapeImp1");
        Shape shape = (Shape) aClass.newInstance();
        shape.todo();
        return "success";
    }
}
enum Singleton {
    INSTANCE;
    public void whateverMethod() {
    }
}