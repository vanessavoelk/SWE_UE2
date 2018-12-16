package at.fhtw.swe.validators;

import at.fhtw.swe.model.ValidationError;
import at.fhtw.swe.service.JsonataEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static at.fhtw.swe.parser.FormParser.getInputKeysInsideGrids;
import static at.fhtw.swe.parser.FormParser.getInputsWithValidations;
import static at.fhtw.swe.configurator.Configurator.setJSONConfig;
import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.validators.DateTimeValidator.validateDateMax;
import static at.fhtw.swe.validators.DateTimeValidator.validateDateMin;
import static at.fhtw.swe.validators.Errors.createError;
import static at.fhtw.swe.validators.NumberValidator.validateMax;
import static at.fhtw.swe.validators.NumberValidator.validateMin;
import static at.fhtw.swe.validators.PatternValidator.validatePattern;
import static at.fhtw.swe.validators.RequiredAttributeValidator.validateRequired;
import static at.fhtw.swe.validators.RowCountValidator.validateMaxRowCount;
import static at.fhtw.swe.validators.RowCountValidator.validateMinRowCount;
import static at.fhtw.swe.validators.StringLengthValidator.validateMaxLength;
import static at.fhtw.swe.validators.StringLengthValidator.validateMinLength;
import static at.fhtw.swe.validators.ValidationInstruction.extractValidationInstruction;

@Service
public class Validator {

    private JsonataEngine jsonataEngine;

    public Validator(JsonataEngine jsonataEngine) {
        this.jsonataEngine = jsonataEngine;
        setJSONConfig();
    }

    public Set<ValidationError> validateForm(
            String form, String formdata, boolean internal) {
        final DocumentContext formContext = JsonPath.parse(form);
        final DocumentContext dataContext = JsonPath.parse(formdata);

        final Object jsonataData = jsonataEngine.parseData(formdata);
        final ArrayNode inputsWithValidations = getInputsWithValidations(formContext);
        final Set<String> gridInputs = getInputKeysInsideGrids(formContext);
        final Set<ValidationError> errors = new HashSet<>();

        inputsWithValidations.forEach(
                input -> {
                    final JsonNode validationNode = input.get(VALIDATE_KEY);
                    final String id = input.get(COMPONENT_KEY).asText();
                    final ArrayNode inspectedValue = dataContext.read("$.." + id, ArrayNode.class);
                    final String type = input.get(TYPE_KEY).asText();

                    if (gridInputs.contains(id)) {
                        // grid validation
                        for (int row = 0; row < inspectedValue.size(); row++) {
                            errors.addAll(
                                    validateSingleValue(inspectedValue.get(row), validationNode, id, type, row, internal));
                            checkJsonnata(jsonataData, validationNode, id, row, internal)
                                    .ifPresent(error -> errors.add(error));
                        }

                    } else {
                        // normal validation
                        errors.addAll(
                                validateSingleValue(inspectedValue.get(0), validationNode, id, type, null, internal));
                        checkJsonnata(jsonataData, validationNode, id, null, internal)
                                .ifPresent(error -> errors.add(error));
                    }
                });

        return errors;
    }

    private Set<ValidationError> validateSingleValue(
            JsonNode valueToInspect,
            JsonNode instruction,
            String inputKey,
            String inputType,
            Integer row,
            boolean internal) {
        final Set<ValidationError> result = new HashSet<>();
        final Consumer<ValidationError> storeError = error -> result.add(error);

        validateRequired(valueToInspect, instruction, inputKey, row, internal)
                .ifPresent(storeError);

        if (TYPE_GRID.equals(inputType)) {
            validateMinRowCount(valueToInspect, instruction, inputKey, row)
                    .ifPresent(storeError);
            validateMaxRowCount(valueToInspect, instruction, inputKey, row)
                    .ifPresent(storeError);
        } else if (TYPE_DATETIME.equals(inputType)) {
            validateDateMin(valueToInspect, instruction, inputKey, row, internal)
                    .ifPresent(storeError);
            validateDateMax(valueToInspect, instruction, inputKey, row, internal)
                    .ifPresent(storeError);
        } else {
            validateMinLength(valueToInspect, instruction, inputKey, row, internal)
                    .ifPresent(storeError);
            validateMaxLength(valueToInspect, instruction, inputKey, row, internal)
                    .ifPresent(storeError);
        }
        validatePattern(valueToInspect, instruction, inputKey, row, internal)
                .ifPresent(storeError);
        validateMin(valueToInspect, instruction, inputKey, row, internal)
                .ifPresent(storeError);
        validateMax(valueToInspect, instruction, inputKey, row, internal)
                .ifPresent(storeError);

        return result;
    }

    private Optional<ValidationError> checkJsonnata(
            Object jsonataData,
            JsonNode validationInstruction,
            String key,
            Integer row,
            boolean internal) {
        final String jsonataPattern =
                extractValidationInstruction(validationInstruction, JSONATA_KEY, internal)
                        .map(JsonNode::asText)
                        .orElse(null);
        if (jsonataPattern != null) {
            String compiledJsonataPattern =
                    Optional.ofNullable(row)
                            .map(rowInt -> rowInt.toString())
                            .map(rowString -> jsonataPattern.replace(ROW_NUM_PLACEHOLER, rowString))
                            .orElse(jsonataPattern);

            compiledJsonataPattern = compiledJsonataPattern.replace(CURRENT_DATE_PLACEHOLER, Instant.now().toString());

            return Optional.ofNullable(jsonataEngine.validate(jsonataData, compiledJsonataPattern))
                    .map(jsonataResult -> Boolean.parseBoolean(jsonataResult))
                    .map(valid -> !valid ? createError(key, row, JSONATA_KEY) : null);
        }

        return Optional.empty();
    }
}