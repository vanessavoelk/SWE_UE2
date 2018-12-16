package at.fhtw.swe;

import java.time.Instant;
import java.util.function.BiFunction;

public class Constants {
    public static final String TYPE_GRID = "grid";
    public static final String COMPONENT_KEY = "id";
    public static final String TYPE_KEY = "type";
    public static final String COMPONENT_TAG = "components";
    public static final String CUSTOM_KEY = "custom";
    public static final String MAX_LENGTH_KEY = "maxLength";
    public static final String VALIDATE_KEY = "validate";
    public static final String REQUIRED_KEY = "required";
    public static final String EXTERNAL_KEY = "external";
    public static final String MIN_KEY = "min";
    public static final String DATE_MAX_KEY = "maxDate";
    public static final String TYPE_DATETIME = "datetime";
    public static final String MAX_KEY = "max";
    public static final String ROW_NUM_PLACEHOLER = "@eval:rownum@";
    public static final String PATTERN_KEY = "pattern";
    public static final String INTERNAL_KEY = "internal";
    public static final String CURRENT_DATE_PLACEHOLER = "@date:now@";
    public static final String JSONATA_KEY = "jsonata";
    public static final String DATE_MIN_KEY = "minDate";
    public static final String GRID_INPUT_KEYS_QUERY =
            "$.." + COMPONENT_TAG + "[?].." + COMPONENT_TAG + "[?]." + COMPONENT_KEY;
    public static final String INPUTS_TO_VALIDATE_QUERY = "$.." + COMPONENT_TAG + "[?]";
    public static final String MIN_LENGTH_KEY = "minLength";
    public static final BiFunction<String, Integer, Boolean>
            MIN_LENGTH_CHECK = (value, length) -> value.length() >= length,
            MAX_LENGTH_CHECK = (value, length) -> value.length() <= length;
    public static final BiFunction<Integer, Integer, Boolean>
            MIN_ROW_COUNT_CHECK = (value, minRowCountVal) -> value >= minRowCountVal,
            MAX_ROW_COUNT_CHECK = (value, maxRowCountVal) -> value <= maxRowCountVal;
    public static final BiFunction<Double, Double, Boolean>
            MIN_CHECK = (value, minVal) -> value >= minVal,
            MAX_CHECK = (value, maxVal) -> value <= maxVal;
    public static final BiFunction<Instant, Instant, Boolean>
            DATE_MIN_CHECK = (value, dateMinVal) -> value.isAfter(dateMinVal),
            DATE_MAX_CHECK = (value, dateMaxVal) -> value.isBefore(dateMaxVal);
}
