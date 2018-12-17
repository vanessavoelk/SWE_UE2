package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public interface Validator {
    static Optional<ValidationError> validate(JsonNode value,
                                                     JsonNode validationInstruction,
                                                     String key,
                                                     Integer row,
                                                     boolean internal) {
        return null;
    };
}
