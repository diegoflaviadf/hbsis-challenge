package br.com.hbsis.challenge.weather;

import java.io.Serializable;

public interface WeatherResponse extends Serializable {

    String getData();

    String getError();

}
