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
        Test test = new Test();
        test.gettest();
    }
}
