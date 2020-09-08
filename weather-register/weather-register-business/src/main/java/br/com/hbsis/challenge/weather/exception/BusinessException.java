package br.com.hbsis.challenge.weather.rules.exception;

import lombok.Getter;

/**
 * Exception para regras de negócio
 */
public class BusinessException extends RuntimeException {

    @Getter
    private final Object[] params;

    public BusinessException(String message, Object... params) {
        super(message);
        this.params = params;
    }
}
