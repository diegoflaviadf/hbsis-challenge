package br.com.hbsis.challenge.weather.controller.handlers;

import br.com.hbsis.challenge.weather.rules.exception.BusinessException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static br.com.hbsis.challenge.weather.config.DefaultMessageSourceConfig.DEFAULT_LOCALE;

/**
 * Cria um {@link ControllerAdvice} global para tratar mensagens de erro
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Mensagem padrão para mensagens de entrada malformada
     */
    private static final String JSON_MALFORMED = "json.malformed";
    private final MessageSource messageSource;
    private final ValidationExceptionHandler validationExceptionHandler;

    public GlobalExceptionHandler(MessageSource messageSource, ValidationExceptionHandler validationExceptionHandler) {
        this.messageSource = messageSource;
        this.validationExceptionHandler = validationExceptionHandler;
    }

    @Override
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String message = messageSource.getMessage(JSON_MALFORMED, null, DEFAULT_LOCALE);
        if (exception.getCause() instanceof InvalidFormatException) {
            return validationExceptionHandler.handleInvalidFormatException((InvalidFormatException) exception.getCause());
        }
        return new ResponseEntity<>(new ApiError(message), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        ApiError errors = new ApiError();
        for (FieldError fieldError : fieldErrors) {
            final String message = messageSource.getMessage(Objects.requireNonNull(fieldError.getDefaultMessage()), null, DEFAULT_LOCALE);
            errors.addError(fieldError.getField(), message);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ApiError> handleBusinessException(BusinessException exception) {
        ApiError error = new ApiError(messageSource.getMessage(exception.getMessage(), exception.getParams(), Locale.getDefault()));
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<>(error, status);
    }

}
