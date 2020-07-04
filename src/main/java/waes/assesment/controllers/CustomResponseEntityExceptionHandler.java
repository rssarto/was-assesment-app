package waes.assesment.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import waes.assesment.exceptions.ApiError;
import waes.assesment.exceptions.DiffDataNotExistsException;
import waes.assesment.exceptions.RecordAlreadyExistsException;
import waes.assesment.exceptions.RecordNotFoundException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String VALIDATION_ERROR = "Validation error";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(VALIDATION_ERROR);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    protected ResponseEntity<Object> handleRecordNotFoundException(final RecordNotFoundException recordNotFoundException){
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, recordNotFoundException);
        return this.buildResponseEntity(apiError);
    }

    @ExceptionHandler(DiffDataNotExistsException.class)
    protected ResponseEntity<Object> handleDiffDataNotExistsException(final DiffDataNotExistsException diffDataNotExistsException){
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, diffDataNotExistsException);
        return this.buildResponseEntity(apiError);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    protected ResponseEntity<Object> handleRecordAlreadyExistsException(final RecordAlreadyExistsException recordAlreadyExistsException){
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, recordAlreadyExistsException);
        return this.buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError){
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
