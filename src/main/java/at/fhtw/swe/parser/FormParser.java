package at.fhtw.swe.parser;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.Set;

import static at.fhtw.swe.Constants.*;
import static at.fhtw.swe.Constants.TYPE_GRID;
import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

public class FormParser {
    private static JsonPath getInputsToValidate = JsonPath.compile(INPUTS_TO_VALIDATE_QUERY, filter(where("@." + VALIDATE_KEY).exists(true)));
    private static JsonPath getInputKeysInsideGrids = JsonPath.compile(GRID_INPUT_KEYS_QUERY, filter(where("@." + TYPE_KEY).eq(TYPE_GRID)), filter(where("@." + VALIDATE_KEY).exists(true)));

    public static ArrayNode getInputsWithValidations(DocumentContext formContext) {
        return formContext.read(getInputsToValidate, ArrayNode.class);
    }

    public static Set<String> getInputKeysInsideGrids(DocumentContext formContext) {
        return formContext.read(getInputKeysInsideGrids, Set.class);
    }
}
