package pl.koder95.bso.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new UniversalErrorMessageFormat(
                status.value(),
                "Entity not found",
                request.getMethod(),
                request.getRequestURI(),
                List.of(ex.getMessage())
        ), new HttpHeaders(), status);
    }

    @ExceptionHandler(DataProcessingException.class)
    protected ResponseEntity<Object> handleDataProcessingException(
            DataProcessingException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        if (ex.getCause() != null) {
            errors.add(getDataProcessingExceptionCauseMessage(ex.getCause()));
        }
        return new ResponseEntity<>(new UniversalErrorMessageFormat(
                status.value(),
                "Data processing failed",
                request.getMethod(),
                request.getRequestURI(),
                errors
        ), new HttpHeaders(), status);
    }

    private String getDataProcessingExceptionCauseMessage(Throwable cause) {
        String message = cause.getMessage();
        if (cause instanceof DataIntegrityViolationException) {
            message = "Data integrity violation";
            Throwable nextCause = cause.getCause();
            Throwable firstCause = nextCause;
            while (nextCause != null) {
                nextCause = nextCause.getCause();
                firstCause = nextCause == null ? firstCause : nextCause;
            }
            if (firstCause != null) {
                message = firstCause.getMessage();
            }
        }
        return message;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        return new ResponseEntity<>(new UniversalErrorMessageFormat(
                status.value(),
                "Method argument is not valid",
                servletRequest.getMethod(),
                servletRequest.getRequestURI(),
                ex.getBindingResult().getAllErrors().stream()
                        .map(this::getErrorMessage)
                        .toList()
        ), headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError fieldError) {
            return fieldError.getField() + " " + e.getDefaultMessage();
        }
        return e.getDefaultMessage();
    }
}
