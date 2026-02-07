package com.example.spring_boot_mode.test;

import lombok.SneakyThrows;

public class TestThread {
    public static void main(String[] args) {
        Thread t1 = new TTread();
        Thread t2 = new TTread();
        t1.start();
        t2.start();

    }
}

class TTread extends Thread{

    public void run() {
        System.out.println("--go");

        System.out.println("--go");
    }
}
