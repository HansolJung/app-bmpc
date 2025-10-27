package it.korea.app_bmpc.common.dto;

import java.util.List;

import org.springframework.validation.FieldError;

import it.korea.app_bmpc.common.utils.TimeFormatUtils;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ApiErrorResponse {
    private final String code;
    private final String message;
    private final String date;
    private final List<ApiFieldError> fieldErrors;

    private ApiErrorResponse(String code, String message, List<ApiFieldError> fieldErrors) {
        this.code = code;
        this.message = message;
        this.date = TimeFormatUtils.getNowTime();
        this.fieldErrors = fieldErrors;
    }

    public static ApiErrorResponse error(String code, String message) {
        return new ApiErrorResponse(code, message, null);
    }

    public static ApiErrorResponse error(String code, String message, List<ApiFieldError> fieldErrors) {
        return new ApiErrorResponse(code, message, fieldErrors);
    }

    @Getter
    public static class ApiFieldError {
        private final String field;
        private final String value;
        private final String message;

        private ApiFieldError(String field, String value, String message) {
            this.field = field;
            this.value = value;
            this.message = message;
        }

        public static ApiFieldError error(FieldError fieldError) {
            return new ApiFieldError(
                    fieldError.getField(),
                    fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                    fieldError.getDefaultMessage());
        }

        public static ApiFieldError error(ConstraintViolation<?> fieldError) {
            String fieldName = fieldError.getPropertyPath().toString();
            fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1);

            return new ApiFieldError(
                    fieldName,
                    fieldError.getInvalidValue() == null ? "" : fieldError.getInvalidValue().toString(),
                    fieldError.getMessage());
        }

        public static ApiFieldError error(String fieldName, String value, String message) {

            return new ApiFieldError(
                    fieldName,
                    value,
                    message);
        }
    }
}
