package com.example.spring_boot_mode.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getStrYmd(String mart,Date date){
        SimpleDateFormat format = new SimpleDateFormat(mart);
        return format.format(date);
    }
}
