package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class DateTimeValidator {
    public static Optional<ValidationError> validateDateTime(
            JsonNode value,
            JsonNode datePickerInstruction,
            String hashKey,
            Integer row,
            String validationKey,
            boolean internal,
            BiFunction<Instant, Instant, Boolean> dateCheck) {
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

    public static Optional<ValidationError> validateDateMin(
            JsonNode value,
            JsonNode datePickerInstruction,
            String key,
            Integer row,
            boolean internal) {
        return validateDateTime(
                value, datePickerInstruction, key, row, DATE_MIN_KEY, internal, DATE_MIN_CHECK);
    }

    public static Optional<ValidationError> validateDateMax(
            JsonNode value,
            JsonNode datePickerInstruction,
            String key,
            Integer row,
            boolean internal) {
        return validateDateTime(
                value, datePickerInstruction, key, row, DATE_MAX_KEY, internal, DATE_MAX_CHECK);
    }
}
