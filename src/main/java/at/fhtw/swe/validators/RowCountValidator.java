package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ExternalValidator.getExternalValidations;

public class RowCountValidator {
    private static String validationKey;
    private static BiFunction<Integer, Integer, Boolean> rowCountCheck;
    private RowCountValidator() {
    }

    public static RowCountValidator createMinRowCountValidator(){
        validationKey = MIN_LENGTH_KEY;
        rowCountCheck = MIN_ROW_COUNT_CHECK;
        return new RowCountValidator();
    }

    public static RowCountValidator createMaxRowCountValidator(){
        validationKey = MAX_LENGTH_KEY;
        rowCountCheck = MAX_ROW_COUNT_CHECK;
        return new RowCountValidator();
    }
    
    public static Optional<ValidationError> validateRowCountValue(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row) {
        if (validationInstruction.has(validationKey)) {

            final Integer val =
                    Optional.ofNullable(getExternalValidations(validationInstruction, validationKey))
                            .map(JsonNode::asInt)
                            .orElse(null);
            return Optional.ofNullable(value)
                    .map(JsonNode::size)
                    .map(valueNumber -> rowCountCheck.apply(valueNumber, val))
                    .map(valid -> valid ? null : createError(hashKey, row, validationKey));
        }

        return Optional.empty();
    }

}
