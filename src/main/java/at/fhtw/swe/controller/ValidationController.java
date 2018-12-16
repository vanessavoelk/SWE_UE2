package at.fhtw.swe.controller;

import at.fhtw.swe.model.ValidationRequestBody;
import at.fhtw.swe.model.ValidationError;
import at.fhtw.swe.validators.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ValidationController {

    private Validator validator;

    public ValidationController(Validator validator) {
        this.validator = validator;
    }

    @GetMapping("/healthCheck")
    public ResponseEntity<Boolean> getHealthCheck(){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/internal")
    public ResponseEntity<Set<ValidationError>> postInternalValidation(@RequestBody() ValidationRequestBody body) {
        Set<ValidationError> validationErrors = this.validator.validateForm(body.getTemplate(), body.getData(), true);
        return ResponseEntity.ok(validationErrors);
    }


    @PostMapping("/external")
    public ResponseEntity<Set<ValidationError>> postExternalValidation(@RequestBody() ValidationRequestBody body) {
        Set<ValidationError> validationErrors = this.validator.validateForm(body.getTemplate(), body.getData(), false);
        return ResponseEntity.ok(validationErrors);
    }
}
