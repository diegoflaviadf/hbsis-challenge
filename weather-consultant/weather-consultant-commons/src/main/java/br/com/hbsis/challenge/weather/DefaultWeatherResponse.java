package br.com.hbsis.challenge.weather;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultWeatherResponse implements WeatherResponse {

    private String data;
    private String error;

}
