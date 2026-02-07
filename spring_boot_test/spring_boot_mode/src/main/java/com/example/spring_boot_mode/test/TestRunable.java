package com.example.spring_boot_mode.test;

import lombok.SneakyThrows;

public class TestRunable {
    public static void main(String[] args) {
        TRunable t1 = new TRunable();
        TRunable t2 = new TRunable();
        t1.run();
        t2.run();
t1.notify();
    }
}

class TRunable implements Runnable {


    public void run() {
        System.out.println("---g");

    }
}