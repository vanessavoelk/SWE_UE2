package at.fhtw.swe.validators;

import com.fasterxml.jackson.databind.JsonNode;

import static at.fhtw.swe.Constants.*;

public class ExternalValidator {
    public static JsonNode getExternalValidations(
            JsonNode validationInstruction, String validationKey) {
        if (JSONATA_KEY.equals(validationKey)) {
            final JsonNode customTag = validationInstruction.get(CUSTOM_KEY);
            if (customTag == null) {
                return null;
            }
            final JsonNode externalTag = customTag.get(EXTERNAL_KEY);
            if (externalTag != null) {
                return externalTag.get(JSONATA_KEY);
            } else if (!customTag.has(INTERNAL_KEY)) {
                // this is a legacy support if custom tag only contains jsonata-string. // TODO: remove this
                return customTag;
            }
        } else {
            return validationInstruction.get(validationKey);
        }

        return null;
    }
}
