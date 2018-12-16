package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class StringLengthValidator {

    public static Optional<ValidationError> validateLength(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row,
            String validationKey,
            boolean internal,
            BiFunction<String, Integer, Boolean> lengthCheck) {
        final Integer length =
                extractValidationInstruction(validationInstruction, validationKey, internal)
                        .map(JsonNode::asInt)
                        .orElse(null);
        if (length != null) {
            return Optional.ofNullable(value)
                    .map(JsonNode::asText)
                    .map(valueString -> lengthCheck.apply(valueString, length))
                    .map(valid -> valid ? null : createError(hashKey, row, validationKey));
        }
        return Optional.empty();
    }

    public static Optional<ValidationError> validateMinLength(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        return validateLength(
                value, validationInstruction, key, row, MIN_LENGTH_KEY, internal, MIN_LENGTH_CHECK);
    }

    public static Optional<ValidationError> validateMaxLength(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        return validateLength(
                value, validationInstruction, key, row, MAX_LENGTH_KEY, internal, MAX_LENGTH_CHECK);
    }

}
