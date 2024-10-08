package com.example.spring_boot_mode.test;

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
        Info<String> info = new InfoImpl<String>();
        info.si();
        info.geti();
    }
}

interface Info<T>{        // 在接口上定义泛型
    public T getVar() ; // 定义抽象方法，抽象方法的返回值就是泛型类型
    public abstract T geti();
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



