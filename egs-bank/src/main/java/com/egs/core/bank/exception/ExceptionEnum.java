package com.egs.core.bank.exception;

public enum ExceptionEnum {
    SERVER_EXCEPTION("Critical error", "Server exception."),
    ENTITY_NOT_FOUND("Entity not found", "Searched entity could not be found using provided data."),
    DATA_VALIDATION_EXCEPTION("Data validation error", "A data validation condition was not met."),
    EXTERNAL_SERVICE_EXCEPTION("External service error", "Error occurred when contacting external service."),
    EXAMPLE_EXCEPTION("Message of error", "Description of error"),
    FORBIDDEN("Access denied", "You don't have permission to execute this operation."),
    ENTITY_ALREADY_EXISTS("Entity already exists", "The entity being saved already exists on the database.");

    private final String message;
    private final String description;

    ExceptionEnum(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}