package com.pklos.myweather;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeStampConverter {
    static String ConvertTimeStampToDate(long timestamp){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp*1000L);
        final String timeString =
                new SimpleDateFormat("HH:mm").format(cal.getTime());

        return timeString;
    }
}
