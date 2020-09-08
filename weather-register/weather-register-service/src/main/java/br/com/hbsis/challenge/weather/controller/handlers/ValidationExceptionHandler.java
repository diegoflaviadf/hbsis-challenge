package br.com.hbsis.challenge.weather.controller.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static br.com.hbsis.challenge.weather.config.DefaultMessageSourceConfig.DEFAULT_LOCALE;

/**
 * Cria um {@link ControllerAdvice} para tratar mensagens de validação
 */
@ControllerAdvice
@Component
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Mensagem padrão para campo com formato inválido
     */
    public static final String FIELD_INVALID_FORMAT = "field.invalid.format";

    private final MessageSource messageSource;

    public ValidationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Trata mensagens com formato inválido
     *
     * @param exception Exception de formato inválido {@link InvalidFormatException}
     * @return {@link ResponseEntity} mensagem tratada
     */
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception) {
        final ApiError errors = new ApiError();
        final JsonMappingException.Reference reference = exception.getPath().get(0);
        final String message = messageSource.getMessage(FIELD_INVALID_FORMAT, null, DEFAULT_LOCALE);
        final String helper = getFieldHelper(reference.getFrom().getClass().getName(), reference.getFieldName());
        errors.addError(reference.getFieldName(), message, helper);
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Retorna uma ajuda para um campo e mensagem
     *
     * @param message Mensagem
     * @param field   Campo
     * @return Ajuda tratada para um campo
     */
    private String getFieldHelper(String message, String field) {
        final String key = message + "." + field + ".helper";
        final String customMessage = messageSource.getMessage(key, null, DEFAULT_LOCALE);
        if (customMessage.equals(key)) {
            return null;
        }
        return customMessage;
    }

}
