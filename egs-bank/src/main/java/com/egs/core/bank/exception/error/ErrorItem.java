package com.egs.core.bank.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;


@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorItem {
    private String field;
    private String message;
    private Object rejectedValue;

    static ErrorItem from(ConstraintViolation<?> constraintViolation) {
        ErrorItem errorItem = new ErrorItem();
        errorItem.setField(constraintViolation.getPropertyPath().toString());
        errorItem.setMessage(constraintViolation.getMessage());
        setRejectedValue(errorItem, constraintViolation.getInvalidValue());
        return errorItem;
    }

    static ErrorItem from(FieldError fieldError) {
        ErrorItem errorItem = new ErrorItem();
        errorItem.setField(fieldError.getField());
        errorItem.setMessage(fieldError.getDefaultMessage());
        setRejectedValue(errorItem, fieldError.getRejectedValue());
        return errorItem;
    }

    static ErrorItem from(ObjectError objectError) {
        ErrorItem errorItem = new ErrorItem();
        errorItem.setField(objectError.getObjectName());
        errorItem.setMessage(objectError.getDefaultMessage());
        return errorItem;
    }

    private static void setRejectedValue(ErrorItem errorItem, Object rejectedValue) {
        // We don't want to return file in ErrorItem
        if (rejectedValue instanceof MultipartFile) {
            rejectedValue = ((MultipartFile) rejectedValue).getOriginalFilename();
        }
        errorItem.setRejectedValue(rejectedValue);
    }
}
