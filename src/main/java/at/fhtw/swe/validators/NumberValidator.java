package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class NumberValidator {
    public static Optional<ValidationError> validateNumberValue(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row,
            String validationKey,
            boolean internal,
            BiFunction<Double, Double, Boolean> numberCheck) {
        final Double val =
                extractValidationInstruction(validationInstruction, validationKey, internal)
                        .map(JsonNode::asDouble)
                        .orElse(null);
        return Optional.ofNullable(value)
                .map(JsonNode::asDouble)
                .map(valueNumber -> numberCheck.apply(valueNumber, val))
                .map(valid -> valid ? null : createError(hashKey, row, validationKey));
    }

    public static Optional<ValidationError> validateMin(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        if (validationInstruction.has(MIN_KEY)) {
            return validateNumberValue(
                    value, validationInstruction, key, row, MIN_KEY, internal, MIN_CHECK);
        }
        return Optional.empty();
    }

    public static Optional<ValidationError> validateMax(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        if (validationInstruction.has(MAX_KEY)) {
            return validateNumberValue(
                    value, validationInstruction, key, row, MAX_KEY, internal, MAX_CHECK);
        }
        return Optional.empty();
    }
}
