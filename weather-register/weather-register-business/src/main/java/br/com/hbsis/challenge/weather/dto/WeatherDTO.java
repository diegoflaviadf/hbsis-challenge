package br.com.hbsis.challenge.weather.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.hbsis.challenge.weather.weather.model.Weather;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class WeatherDTO {

    private UUID id;
    private LocalDateTime createdAt;
    private Weather.Attributes attrs;

}
