package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.Constants.DATE_MAX_CHECK;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class PatternValidator implements Validator{
    public static Optional<ValidationError> validate(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        final String regexPattern =
                extractValidationInstruction(validationInstruction, PATTERN_KEY, internal)
                        .map(JsonNode::asText)
                        .orElse(null);
        if (regexPattern != null) {
            return Optional.ofNullable(value)
                    .map(JsonNode::asText)
                    .map(valueString -> valueString.matches(regexPattern))
                    .map(valid -> valid ? null : createError(key, row, PATTERN_KEY));
        }

        return Optional.empty();
    }
}
