package com.tks.gwa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeHelper {
    public static String dateConvertToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static Date stringConvertToDate(String dateStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return  sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long diffInMilliseconds(Date from, Date to){

        long diffInMillies = Math.abs(to.getTime() - from.getTime());

        return diffInMillies;
    }
}
