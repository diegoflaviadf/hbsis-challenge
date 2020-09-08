package br.com.hbsis.challenge.weather;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultWeatherRequestParams implements WeatherRequestParams {

    private Integer lat;
    private Integer lon;

}
