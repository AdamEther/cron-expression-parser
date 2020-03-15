package engineering.deliveroo.cron;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.Collectors;

class Field {
    private final FieldType fieldType;
    private final BitSet bitSet;

    Field(FieldType fieldType, String fieldExpression) {
        this.fieldType = fieldType;
        this.bitSet = new BitSet(fieldType.max + 1);
        parse(fieldExpression);
    }

    private void parse(String field) {
        String[] fieldParts = field.split(",");
        for (String fieldPart : fieldParts) {
            if (fieldPart.contains("/")) {
                String[] split = fieldPart.split("/");
                if (split.length > 2) {
                    throw new IllegalArgumentException("More than one slashes, field: " + fieldPart);
                }
                int[] range = getRange(split[0]);
                if (!split[0].contains("-")) {
                    range[1] = fieldType.max;
                }
                int increment = Integer.parseInt(split[1]);
                if (increment <= 0) {
                    throw new IllegalArgumentException("Increment must be more zero, field: " + fieldPart);
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
            result[0] = fieldType.min;
            result[1] = fieldType.max;
            return result;
        }
        if (fieldPart.contains("-")) {
            String[] split = fieldPart.split("-");
            if (split.length > 2) {
                throw new IllegalArgumentException("Range has more than two fields: " + fieldPart);
            }
            result[0] = Integer.parseInt(split[0]);
            result[1] = Integer.parseInt(split[1]);
        } else {
            int fieldPartValue = Integer.parseInt(fieldPart);
            result[0] = fieldPartValue;
            result[1] = fieldPartValue;
        }
        if (result[0] > result[1]) {
            throw new IllegalArgumentException("Range start more than end, field: " + fieldPart);
        }
        if (result[0] < fieldType.min) {
            throw new IllegalArgumentException("Range start less than min [" + fieldType.min + "], field: " + fieldPart);
        }
        if (result[0] > fieldType.max || result[1] > fieldType.max) {
            throw new IllegalArgumentException("Range end more than max [" + fieldType.max + "], field: " + fieldPart);
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
