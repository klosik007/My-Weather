package com.pklos.myweather.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStampConverter {
    private static String _pattern = "HH:ss";
    public static void setPattern(String pattern) { _pattern = pattern; }

    public static String ConvertTimeStampToDate(long timestamp){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp*1000L);
        final String timeString =
                new SimpleDateFormat(_pattern).format(cal.getTime());

        return timeString;
    }
}
