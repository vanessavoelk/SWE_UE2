package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.function.BiFunction;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

public class NumberValidator implements Validator {
    private static String validationKey;
    private static BiFunction<Double, Double, Boolean> numberCheck;

    private NumberValidator() {
    }

    public static NumberValidator createMinNumberValidator(){
        validationKey = MIN_KEY;
        numberCheck = MIN_CHECK;
        return new NumberValidator();
    }

    public static NumberValidator createMaxNumberValidator(){
        validationKey = MAX_KEY;
        numberCheck = MAX_CHECK;
        return new NumberValidator();
    }

    public static Optional<ValidationError> validate(
            JsonNode value,
            JsonNode validationInstruction,
            String hashKey,
            Integer row,
            boolean internal) {
        if (validationInstruction.has(validationKey)) {

            final Double val =
                    extractValidationInstruction(validationInstruction, validationKey, internal)
                            .map(JsonNode::asDouble)
                            .orElse(null);
            return Optional.ofNullable(value)
                    .map(JsonNode::asDouble)
                    .map(valueNumber -> numberCheck.apply(valueNumber, val))
                    .map(valid -> valid ? null : createError(hashKey, row, validationKey));
        }
        return Optional.empty();
    }
}
