package com.yoxaron.webapp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainDate {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Date date = new Date();
        System.out.println(date);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(date.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        System.out.println(calendar.getTime());

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        System.out.println(ldt);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(sdf.format(date));

        DateTimeFormatter dtf1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        System.out.println(dtf1.format(ldt));

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println(dtf2.format(ldt));
    }
}
