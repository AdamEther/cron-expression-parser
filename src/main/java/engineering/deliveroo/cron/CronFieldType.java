package engineering.deliveroo.cron;

public enum CronFieldType {
    MINUTE(0, 59, 60),
    HOUR(0, 23, 24),
    DAY_OF_MONTH(1, 31, 31),
    MONTH(1, 12, 12),
    DAY_OF_WEEK(0, 6, 7);

    final int min;
    final int max;
    final int length;

    CronFieldType(int min, int max, int length) {
        this.min = min;
        this.max = max;
        this.length = length;
    }

    public int parse(String atomicPart) {
        try {
            return Integer.parseInt(atomicPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cron field part: " + atomicPart, e);
        }
    }
}