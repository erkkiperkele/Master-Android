package com.erkkiperkele.master_android;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateTimeProvider {

    private final SimpleDateFormat _dateFormatter;

    // WARNING: singleton pattern -> make sure '_instance' is the last field else runtime error
    private final static DateTimeProvider _ourInstance = new DateTimeProvider();

    private DateTimeProvider() {
        _dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
    }

    public static DateTimeProvider getInstance() {

        return _ourInstance;
    }

    public Long getTimeStampNow() {

        return new java.util.Date().getTime();
    }

    public String getPrettyDateTime(long timeStamp){

        return _dateFormatter.format(timeStamp);
    }
}