package engineering.deliveroo.cron;

import org.junit.jupiter.api.Test;

import static engineering.deliveroo.cron.FieldType.MINUTE;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CronExpressionTest {
    @Test
    void parseNumber() {
        Field field = new Field(MINUTE, "0");
        assertValues(field, 0);
    }

    @Test
    void parseNumberWithIncrement() {
        Field field = new Field(MINUTE, "0/15");
        assertValues(field, 0, 15, 30, 45);
    }

    @Test
    void parseAsteriskWithIncrementMinutes() {
        Field field = new Field(MINUTE, "*/15");
        assertValues(field, 0, 15, 30, 45);
    }

    @Test
    void parseRange() {
        Field field = new Field(FieldType.DAY_OF_WEEK, "1-5");
        assertValues(field, 1, 2, 3, 4, 5);
    }

    @Test
    void parseRangeWithIncrement() {
        Field field = new Field(MINUTE, "0-10/2");
        assertValues(field, 0, 2, 4, 6, 8, 10);
    }

    @Test
    void parseCollection() {
        Field field = new Field(MINUTE, "1,15");
        assertValues(field, 1, 15);
    }

    @Test
    void parseAsteriskMinutes() {
        Field field = new Field(MINUTE, "*");
        assertValues(field, rangeClosed(0, 59).toArray());
    }

    @Test
    void parseAsteriskHours() {
        Field field = new Field(FieldType.HOUR, "*");
        assertValues(field, rangeClosed(0, 23).toArray());
    }

    @Test
    void parseAsteriskDayOfMonth() {
        Field field = new Field(FieldType.DAY_OF_MONTH, "*");
        assertValues(field, rangeClosed(1, 31).toArray());
    }

    @Test
    void parseAsteriskDayOfWeek() {
        Field field = new Field(FieldType.DAY_OF_WEEK, "*");
        assertValues(field, rangeClosed(0, 6).toArray());
    }

    @Test
    void parseAsteriskMonth() {
        Field field = new Field(FieldType.MONTH, "*");
        assertValues(field, rangeClosed(1, 12).toArray());
    }

    @Test
    void parseAsteriskWithIncrementMonth() {
        Field field = new Field(FieldType.MONTH, "*/2");
        assertValues(field, 1, 3, 5, 7, 9, 11);
    }

    @Test
    void parseInvalidNumberOfFieldsLessThanFive() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0 0 *"));
    }

    @Test
    void parseInvalidNumberOfFieldsMoreThanFive() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0 0 0 0 0 *"));
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
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("60 * * * *"));
    }

    @Test
    void parseInvalidHours() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* 24 * * *"));
    }

    @Test
    void parseInvalidDayOfMonth() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * 32 * *"));
    }

    @Test
    void parseInvalidMonth() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * * 13 *"));
    }

    @Test
    void parseInvalidDayOfWeek() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("* * * * 7"));
    }

    @Test
    void parseInvalidRange() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("50-61 * * * *"));
    }

    @Test
    void parseInvalidDelimiter() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("10;20 * * * *"));
    }

    @Test
    void parseInvalidPeriod() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("20-10 * * * *"));
    }

    @Test
    void parseInvalidSlashes() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/5/15 * * * *"));
    }

    @Test
    void parseInvalidIncrementNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/-15 * * * *"));
    }

    @Test
    void parseInvalidIncrementZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("*/0 * * * *"));
    }

    @Test
    void parseInvalidRangeDoubleMinus() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CronExpression("0-5-15 * * * *"));
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

    // less than min value
    // negative, max / min integer + 1

    private void assertValues(Field field, int... values) {
        assertArrayEquals(values, field.values());
    }
}