package at.fhtw.swe.validators;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

import static at.fhtw.swe.validators.ExternalValidator.getExternalValidations;
import static at.fhtw.swe.validators.InternalValidator.getInternalValidations;

public class ValidationInstruction {
    public static Optional<JsonNode> extractValidationInstruction(
            JsonNode validationInstruction, String validationKey, boolean internal) {
        return internal ? Optional.ofNullable(
                getInternalValidations(validationInstruction, validationKey))
                : Optional.ofNullable(
                getExternalValidations(validationInstruction, validationKey));
    }
}
