package com.yoxaron.webapp.util;

import com.yoxaron.webapp.model.Period;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate of(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static String getStringFromPeriod(Period period) {
        StringBuilder sb = new StringBuilder();
        LocalDate begin = period.getBegin();
        LocalDate end = period.getEnd();

        sb.append(begin.format(DATE_TIME_FORMATTER));
        sb.append(" - ");
        sb.append(end.equals(NOW) ? "Настоящее время": end.format(DATE_TIME_FORMATTER));

        return sb.toString();
    }
    
    private DateUtil() {}
}
