package engineering.deliveroo.cron;

public enum FieldType {
    MINUTE(0, 59),
    HOUR(0, 23),
    DAY_OF_MONTH(1, 31),
    MONTH(1, 12),
    DAY_OF_WEEK(0, 6);

    final int min;
    final int max;

    FieldType(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
