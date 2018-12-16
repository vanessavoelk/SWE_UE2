package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ExternalValidator.getExternalValidations;

public class RowCountValidator {
    public static Optional<ValidationError> validateRowCountValue(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row,
            String validationKey,
            BiFunction<Integer, Integer, Boolean> rowCountCheck) {
        final Integer val =
                Optional.ofNullable(getExternalValidations(validationInstruction, validationKey))
                        .map(JsonNode::asInt)
                        .orElse(null);
        return Optional.ofNullable(value)
                .map(JsonNode::size)
                .map(valueNumber -> rowCountCheck.apply(valueNumber, val))
                .map(valid -> valid ? null : createError(hashKey, row, validationKey));
    }

    public static Optional<ValidationError> validateMinRowCount(
            JsonNode value, JsonNode validationInstruction, String key, Integer row) {
        if (validationInstruction.has(MIN_LENGTH_KEY)) {
            return validateRowCountValue(
                    value, validationInstruction, key, row, MIN_LENGTH_KEY, MIN_ROW_COUNT_CHECK);
        }
        return Optional.empty();
    }

    public static Optional<ValidationError> validateMaxRowCount(
            JsonNode value, JsonNode validationInstruction, String key, Integer row) {
        if (validationInstruction.has(MAX_LENGTH_KEY)) {
            return validateRowCountValue(
                    value, validationInstruction, key, row, MAX_LENGTH_KEY, MAX_ROW_COUNT_CHECK);
        }
        return Optional.empty();
    }

}
