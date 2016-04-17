package com.peace.twitsec.app.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse extends Response {

    private String errorCode;

    private String consumerMessage;

    private String applicationMessage;

    private List<ValidationError> validationErrors;

    public ErrorResponse() {
        super("error");
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getConsumerMessage() {
        return consumerMessage;
    }

    public ErrorResponse setConsumerMessage(String consumerMessage) {
        this.consumerMessage = consumerMessage;
        return this;
    }

    public String getApplicationMessage() {
        return applicationMessage;
    }

    public ErrorResponse setApplicationMessage(String applicationMessage) {
        this.applicationMessage = applicationMessage;
        return this;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public ErrorResponse setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
        return this;
    }
}

