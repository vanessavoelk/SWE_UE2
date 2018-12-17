package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class DateTimeValidator implements Validator{
    private static String validationKey;
    private static BiFunction<Instant, Instant, Boolean> dateCheck;

    private DateTimeValidator() {
    }

    public static DateTimeValidator createMinDateValidator(){
        validationKey = DATE_MIN_KEY;
        dateCheck = DATE_MIN_CHECK;
        return new DateTimeValidator();
    }

    public static DateTimeValidator createMaxDateValidator(){
        validationKey = DATE_MAX_KEY;
        dateCheck = DATE_MAX_CHECK;
        return new DateTimeValidator();
    }

    public static Optional<ValidationError> validate(
            JsonNode value,
            JsonNode datePickerInstruction,
            String hashKey,
            Integer row,
            boolean internal) {

        final String currDate =
                extractValidationInstruction(datePickerInstruction, validationKey, internal)
                        .map(JsonNode::asText)
                        .orElse(null);
        if (currDate != null) {
            return Optional.ofNullable(value)
                    .map(JsonNode::asText)
                    .map(valueString -> dateCheck.apply(Instant.parse(valueString), Instant.parse(currDate)))
                    .map(valid -> valid ? null : createError(hashKey, row, validationKey));
        }
        return Optional.empty();
    }
}
