package java.engineering.deliveroo.cron;

import java.util.StringJoiner;

public class CronExpression {
    private final Field munutes;
    private final Field hours;
    private final Field daysOfMonth;
    private final Field months;
    private final Field daysOfWeek;
    private final String command;

    public CronExpression(String expression) {
        if (expression == null || "".equals(expression)) {
            throw new IllegalArgumentException("Expression must be non empty string");
        }
        String[] fields = expression.trim().split(" ");
        if (!areValidFields(fields)) {
            throw new IllegalArgumentException("Expression must consists of 6 fields");
        }
        munutes = new Field(FieldType.MINUTE, fields[0]);
        hours = new Field(FieldType.HOUR, fields[1]);
        daysOfMonth = new Field(FieldType.DAY_OF_MONTH, fields[2]);
        months = new Field(FieldType.MONTH, fields[3]);
        daysOfWeek = new Field(FieldType.DAY_OF_WEEK, fields[4]);
        command = fields[5];
    }

    private static boolean areValidFields(String[] fields) {
        return fields != null && fields.length == 6;
    }

    @Override
    public String toString() {
        return new StringJoiner("\n")
                .add("minute        " + munutes)
                .add("hour          " + hours)
                .add("day of month  " + daysOfMonth)
                .add("month         " + months)
                .add("day of week   " + daysOfWeek)
                .add("command       " + command)
                .toString();
    }
}
