package com.yoxaron.webapp.util;

import com.yoxaron.webapp.model.Period;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
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

    public static LocalDate parse(String date) {
        if (date == null || date.trim().isEmpty() || "Сейчас".equals(date)) {
            return NOW;
        }
        YearMonth yearMonth = YearMonth.parse(date, DATE_TIME_FORMATTER);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }

    public static String getStringFromDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.equals(NOW) ? "Сейчас" : date.format(DATE_TIME_FORMATTER);
    }

    public static String getStringFromPeriod(Period period) {
        LocalDate begin = period.getBegin();
        LocalDate end = period.getEnd();

        String endString = end.equals(NOW) ? "Сейчас" : end.format(DATE_TIME_FORMATTER);

        return String.format("%s - %s", begin.format(DATE_TIME_FORMATTER), endString);
    }
    
    private DateUtil() {}
}
