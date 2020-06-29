package com.adamether.cron;

import java.util.StringJoiner;

import static com.adamether.cron.CronFieldType.*;

public class CronExpression {
    private final CronField minutes;
    private final CronField hours;
    private final CronField daysOfMonth;
    private final CronField months;
    private final CronField daysOfWeek;
    private final String command;

    public CronExpression(String expression) {
        if (expression == null || "".equals(expression)) {
            throw new IllegalArgumentException("Expression must be non empty string");
        }
        String[] fields = expression.trim().split(" ");
        if (!areValidFields(fields)) {
            throw new IllegalArgumentException("Expression must consists of 6 fields");
        }
        minutes     = new CronField(MINUTE, fields[0]);
        hours       = new CronField(HOUR, fields[1]);
        daysOfMonth = new CronField(DAY_OF_MONTH, fields[2]);
        months      = new CronField(MONTH, fields[3]);
        daysOfWeek  = new CronField(DAY_OF_WEEK, fields[4]);
        command     = fields[5];
    }

    private static boolean areValidFields(String[] fields) {
        return fields != null && fields.length == 6;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n")
                .add("minute        " + minutes)
                .add("hour          " + hours)
                .add("day of month  " + daysOfMonth)
                .add("month         " + months)
                .add("day of week   " + daysOfWeek)
                .add("command       " + command)
                .toString();
    }
}