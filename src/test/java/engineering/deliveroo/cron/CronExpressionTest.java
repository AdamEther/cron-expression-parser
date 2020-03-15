package engineering.deliveroo.cron;

import org.junit.jupiter.api.Test;

import static engineering.deliveroo.cron.CronFieldType.*;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CronExpressionTest {
    @Test
    void parseNumber() {
        CronField cronField = new CronField(MINUTE, "0");
        assertValues(cronField, 0);
    }

    @Test
    void parseNumberWithIncrement() {
        CronField cronField = new CronField(MINUTE, "0/15");
        assertValues(cronField, 0, 15, 30, 45);
    }

    @Test
    void parseAsteriskWithIncrementMinutes() {
        CronField cronField = new CronField(MINUTE, "*/15");
        assertValues(cronField, 0, 15, 30, 45);
    }

    @Test
    void parseRange() {
        CronField cronField = new CronField(DAY_OF_WEEK, "1-5");
        assertValues(cronField, 1, 2, 3, 4, 5);
    }

    @Test
    void parseRangeWithIncrement() {
        CronField cronField = new CronField(MINUTE, "0-10/2");
        assertValues(cronField, 0, 2, 4, 6, 8, 10);
    }

    @Test
    void parseCollection() {
        CronField cronField = new CronField(MINUTE, "1,15");
        assertValues(cronField, 1, 15);
    }

    @Test
    void parseAsteriskMinutes() {
        CronField cronField = new CronField(MINUTE, "*");
        assertValues(cronField, rangeClosed(0, 59).toArray());
    }

    @Test
    void parseAsteriskHours() {
        CronField cronField = new CronField(HOUR, "*");
        assertValues(cronField, rangeClosed(0, 23).toArray());
    }

    @Test
    void parseAsteriskDayOfMonth() {
        CronField cronField = new CronField(DAY_OF_MONTH, "*");
        assertValues(cronField, rangeClosed(1, 31).toArray());
    }

    @Test
    void parseAsteriskDayOfWeek() {
        CronField cronField = new CronField(DAY_OF_WEEK, "*");
        assertValues(cronField, rangeClosed(0, 6).toArray());
    }

    @Test
    void parseAsteriskMonth() {
        CronField cronField = new CronField(MONTH, "*");
        assertValues(cronField, rangeClosed(1, 12).toArray());
    }

    @Test
    void parseAsteriskWithIncrementMonth() {
        CronField cronField = new CronField(MONTH, "*/2");
        assertValues(cronField, 1, 3, 5, 7, 9, 11);
    }

    @Test
    void parseInvalidNumberOfFieldsLessThanSix() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0 0 *"));
    }

    @Test
    void parseInvalidNumberOfFieldsMoreThanSix() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0 0 0 0 0 * /usr/bin/find"));
    }

    @Test
    void parseInvalidEmptyString() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression(""));
    }

    @Test
    void parseInvalidNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression(null));
    }

    @Test
    void parseInvalidNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("null"));
    }

    @Test
    void parseInvalidMinutes() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("60 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidHours() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* 24 * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidHoursNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("-1 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidDayOfMonth() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * 32 * * /usr/bin/find"));
    }

    @Test
    void parseInvalidDayOfMonthZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * 0 * * /usr/bin/find"));
    }

    @Test
    void parseInvalidMonth() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * * 13 * /usr/bin/find"));
    }

    @Test
    void parseInvalidZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * * 0 * /usr/bin/find"));
    }

    @Test
    void parseInvalidDayOfWeek() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * * * 7 /usr/bin/find"));
    }

    @Test
    void parseInvalidRange() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("50-61 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidDelimiter() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("10;20 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidPeriod() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("20-10 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidSlashes() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/5/15 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidIncrementNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/-15 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidIncrementZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/0 * * * * /usr/bin/find"));
    }

    @Test
    void parseInvalidRangeDoubleMinus() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0-5-15 * * * * /usr/bin/find"));
    }

    @Test
    void parseIntMaxValuePlusOne() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("2147483648 * * * * /usr/bin/find"));
    }

    @Test
    void parseIntMinValueMinusOne() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("-2147483649 * * * * /usr/bin/find"));
    }

    @Test
    void parseExpressionEntirely() {
        CronExpression cronExpression = new CronExpression("  */15 0 1,15 * 1-5 /usr/bin/find   ");
        String expected =
                "minute        0 15 30 45\n" +
                "hour          0\n" +
                "day of month  1 15\n" +
                "month         1 2 3 4 5 6 7 8 9 10 11 12\n" +
                "day of week   1 2 3 4 5\n" +
                "command       /usr/bin/find";

        assertEquals(expected, cronExpression.toString());
    }

    private void assertValues(CronField cronField, int... values) {
        assertArrayEquals(values, cronField.values());
    }
}