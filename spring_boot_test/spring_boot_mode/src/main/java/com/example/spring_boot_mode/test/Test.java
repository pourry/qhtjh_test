package com.example.spring_boot_mode.test;

import cn.hutool.core.lang.copier.Copier;

import javax.swing.text.TabableView;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test extends Testf {
    public Test(){
        super("123");
        super.s();
    }
    public  void  gettest(){
        super.s();
    }
}
class Testf {
    String str = "1111";
    String ss ;

    public Testf(String ss){
        this.ss = ss;
    }

    @Override
    public String toString() {
        return "Testf{" +
                "str='" + str + '\'' +
                "ss='" + ss + '\'' +
                '}';
    }
    public void s(){
        System.out.println( "str='" + str  +"ss='" + ss );
    }
}
class tttt {

    public static void main(String[] args) {
        Arrays.asList(1,2,3);
        String[][] s = new String[2][];
        s[0] = new String[2];
        s[1] = new String[3];
        s[0][0] = "Good";
        s[0][1] = "Luck";
        s[1][0] = "to";
        s[1][1] = "you";
        s[1][2] = "!";

        String prefix = "Hello, ";
        Function<String, String> greeter2 = prefix::concat;
        System.out.println(greeter2.apply("1233"));


    }
}

interface Info<T>{        // 在接口上定义泛型
    T getVar() ; // 定义抽象方法，抽象方法的返回值就是泛型类型
    T geti();
    default void si(){
        System.out.println("静态方法");
    }
}
class InfoImpl<T> implements Info<T> {
    @Override
    public T getVar() {
        return null;
    }

    @Override
    public T geti() {
        System.out.println("geti执行");
        return null;
    }
}

class TestIt
{
    public static void main ( String[] args ) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        System.out.println(s);
        int[] myArray = {1, 2, 3, 4, 5};
        ChangeIt.doIt( myArray );
        for(int j=0; j<myArray.length; j++)
            System.out.print( myArray[j] + " " );
    }
}
@Deprecated
class ChangeIt
{
    static void doIt( int[] z )
    {
        z[0] = 11;
        z = null;

    }
}

 class BRRead {
         public static void main(String[] args) {
            ThreadLocal<String> st = new  ThreadLocal<>();
            st.set("2222");
            st.get();
            st.remove();
             Scanner scan = new Scanner(System.in);
             // 从键盘接收数据

             // next方式接收字符串
             System.out.println("next方式接收：");
             // 判断是否还有输入
             if (scan.hasNext()) {
                 String str1 = scan.next();
                 System.out.println("输入的数据为：" + str1);
             }
             scan.close();
         }

}
 class RunoobTest {

     public static void main(String[] args) throws ExecutionException, InterruptedException {

         CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
             System.out.println("111");
         });
         //等待 voidCompletableFuture 完成
         CompletableFuture.allOf(voidCompletableFuture);
         voidCompletableFuture.get();

         List<Testobject> list = new ArrayList<>();
         list.add(new Testobject("aaa", 11));
         list.add(new Testobject("a22", 11));
         list.add(new Testobject("bbb", 22));
         list.add(new Testobject("cc", 33));

         list.forEach(item -> {

             if (item.getAge()>11){
                 System.out.println("年龄太大了");
                 return;
             }
             System.out.println(item.getName());
         });

         Map<String, Integer> map = new HashMap<>();
         map.put("Apple", 10);
         map.put("Banana", 20);
         map.put("Orange", 30);
         Set<Map.Entry<String, Integer>> entries = map.entrySet();
         Set<String> set = new HashSet<>();
         set.add("Apple");
         set.add("Banana");
         set.add("Orange");

         Testobject testobject = new Testobject("rr",12);
         Function<Integer, String> func2 = String::valueOf;
         Function<Integer,String> g = (x)->{return x*2+"";};
         String apply = g.apply(2);
         DoubleFunction<Float> doubleDoubleFunction = x->{return  Float.valueOf((x+2)+"");};
         System.out.println(doubleDoubleFunction.apply(12));
         Supplier<String> su = ()->{
             System.out.println("123");
             return "12344";
         };
         Supplier<String> su2 = String::new;
         System.out.println(su2.get());
         final Car car = Car.create(Car::new);

     }
 }
     class Testobject{
      private String name;
      private int age;

         public Testobject() {
         }
         public  String getstr(){
             return "12t4";
         }
         public Testobject(String name, int age) {
             this.name = name;
             this.age = age;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getAge() {
             return age;
         }

         public void setAge(int age) {
             this.age = age;
         }

         @Override
         public String toString() {
             return this.getName() + "----" + this.getAge();
         }
     }

class Singleton {
    private static Singleton instance;
    private static final String Test = "";
    private Singleton (){}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

class Car {
    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this);
    }
}

