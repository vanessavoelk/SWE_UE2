package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

import static at.fhtw.swe.Constants.REQUIRED_KEY;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class RequiredAttributeValidator {
    public static Optional<ValidationError> validateRequired(
            JsonNode value,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        if (extractValidationInstruction(validationInstruction, REQUIRED_KEY, internal)
                .map(JsonNode::asBoolean)
                .orElse(false)) {

            if (value == null) {
                return Optional.ofNullable(createError(key, row, REQUIRED_KEY));
            }
            return Optional.ofNullable(value)
                    .map(JsonNode::asText)
                    .map(valueString -> !valueString.isEmpty())
                    .map(valid -> valid ? null : createError(key, row, REQUIRED_KEY));
        }

        return Optional.empty();
    }
}
