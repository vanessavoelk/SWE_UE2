package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;

public class Errors {

    public static ValidationError createError(String key, Integer row, String violation) {
        final ValidationError error = new ValidationError().key(key).violation(violation);
        return error;
    }
}
