package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class StringLengthValidator {
    private static String validationKey;
    private static BiFunction<String, Integer, Boolean> lengthCheck;
    private StringLengthValidator() {
    }

    public static StringLengthValidator createMinStringLengthValidator(){
        validationKey = MIN_LENGTH_KEY;
        lengthCheck = MIN_LENGTH_CHECK;
        return new StringLengthValidator();
    }

    public static StringLengthValidator createMaxStringLengthValidator(){
        validationKey = MAX_LENGTH_KEY;
        lengthCheck = MAX_LENGTH_CHECK;
        return new StringLengthValidator();
    }

    public static Optional<ValidationError> validateLength(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row,
            boolean internal) {
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

}
