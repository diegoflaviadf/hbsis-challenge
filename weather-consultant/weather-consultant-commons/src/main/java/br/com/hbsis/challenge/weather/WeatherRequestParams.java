package br.com.hbsis.challenge.weather;

import java.io.Serializable;

public interface WeatherRequestParams extends Serializable {

    Integer getLat();

    Integer getLon();

}
