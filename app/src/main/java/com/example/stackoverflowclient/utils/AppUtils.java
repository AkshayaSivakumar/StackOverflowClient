package com.example.stackoverflowclient.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {
    public static String convertToLocalDateTime(long creationDate, String dateTimeFormat) {
        Date date = new Date(creationDate * 1000L);
        SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
        return format.format(date);
    }
}
