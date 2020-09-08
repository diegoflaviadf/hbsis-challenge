package br.com.hbsis.challenge.weather.dto;

import br.com.hbsis.challenge.weather.rules.ErrorConstraints;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class WeatherRegistrationDTO implements Serializable {

    @NotNull(message = ErrorConstraints.FIELD_NOT_NULL)
    private Integer lat;

    @NotNull(message = ErrorConstraints.FIELD_NOT_NULL)
    private Integer lon;

}
