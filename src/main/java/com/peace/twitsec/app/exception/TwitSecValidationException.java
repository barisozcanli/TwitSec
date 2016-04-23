package com.peace.twitsec.app.exception;

import com.peace.twitsec.app.response.ErrorResponse;
import com.peace.twitsec.app.response.ValidationError;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TwitSecValidationException extends RuntimeException {

    private final int status = 400;
    private final String errorCode = "100";
    private String errorMessage;
    private String developerMessage;
    private List<ValidationError> errors = new ArrayList<ValidationError>();

    public TwitSecValidationException() {
        errorMessage = "Validation Error";
        developerMessage = "The data passed in the request was invalid. Please check and resubmit";
    }

    public TwitSecValidationException(String message) {
        super();
        errorMessage = message;
    }

    public TwitSecValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this();
        for(ConstraintViolation<?> constraintViolation : violations) {
            ValidationError error = new ValidationError();
            error.setMessage(constraintViolation.getMessage());
            error.setPropertyName(constraintViolation.getPropertyPath().toString());
            error.setPropertyValue(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null);
            errors.add(error);
        }
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(errorCode);
        response.setApplicationMessage(developerMessage);
        response.setConsumerMessage(errorMessage);
        response.setValidationErrors(errors);
        return response;
    }

}

