package at.fhtw.swe.validators;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

import static at.fhtw.swe.Constants.CUSTOM_KEY;
import static at.fhtw.swe.Constants.INTERNAL_KEY;

public class InternalValidator {

    public static JsonNode getInternalValidations(
            JsonNode validationInstruction, String validationKey) {
        return Optional.ofNullable(validationInstruction.get(CUSTOM_KEY))
                .map(customTag -> customTag.get(INTERNAL_KEY))
                .map(internalTag -> internalTag.get(validationKey))
                .orElse(null);
    }
}
