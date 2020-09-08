package br.com.hbsis.challenge.weather;

public interface WeatherConsultant {

    WeatherResponse find(WeatherRequestParams params);

}
