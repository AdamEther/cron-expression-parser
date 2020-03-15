package engineering.deliveroo.cron;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.Collectors;

class CronField {
    private final CronFieldType type;
    private final BitSet bitSet;

    CronField(CronFieldType type, String fieldExpression) {
        this.type = type;
        this.bitSet = new BitSet(type.length);
        parse(fieldExpression);
    }

    private void parse(String field) {
        String[] fieldParts = field.split(",");
        for (String fieldPart : fieldParts) {
            if (fieldPart.contains("/")) {
                String[] split = fieldPart.split("/");
                if (split.length > 2) {
                    throw new IllegalArgumentException("More than one slash, field: " + fieldPart);
                }
                int[] range = getRange(split[0]);
                if (!split[0].contains("-")) {
                    range[1] = type.max;
                }
                int increment = Integer.parseInt(split[1]);
                if (increment <= 0) {
                    throw new IllegalArgumentException("Increment must be more than zero, field: " + fieldPart);
                }
                for (int i = range[0]; i <= range[1]; i += increment) {
                    bitSet.set(i);
                }
            } else {
                int[] range = getRange(fieldPart);
                bitSet.set(range[0], range[1] + 1);
            }
        }
    }

    private int[] getRange(String fieldPart) {
        int[] result = new int[2];
        if (fieldPart.contains("*")) {
            result[0] = type.min;
            result[1] = type.max;
            return result;
        }
        if (fieldPart.contains("-")) {
            String[] split = fieldPart.split("-");
            if (split.length > 2) {
                throw new IllegalArgumentException("Range has more than two fields: " + fieldPart);
            }
            result[0] = type.parse(split[0]);
            result[1] = type.parse(split[1]);
        } else {
            int fieldPartValue = type.parse(fieldPart);
            result[0] = fieldPartValue;
            result[1] = fieldPartValue;
        }
        if (result[0] > result[1]) {
            throw new IllegalArgumentException("Range start more than end, field: " + fieldPart);
        }
        if (result[0] < type.min) {
            throw new IllegalArgumentException("Range start less than min [" + type.min + "], field: " + fieldPart);
        }
        if (result[0] > type.max || result[1] > type.max) {
            throw new IllegalArgumentException("Range end more than max [" + type.max + "], field: " + fieldPart);
        }
        return result;
    }

    int[] values() {
        return bitSet.stream().toArray();
    }

    private String join(int[] values) {
        return Arrays.stream(values).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return join(values());
    }
}