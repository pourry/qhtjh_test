package com.example.spring_boot_mode.interview_question;

public class Singlation {
            private static Singlation s;
            private Singlation(){};
    public static Singlation getInstance(){
        if(s == null){
            s = new Singlation();
        }
        return s;
    }

}
