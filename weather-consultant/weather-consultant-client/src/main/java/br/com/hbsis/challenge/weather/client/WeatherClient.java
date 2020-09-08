package br.com.hbsis.challenge.weather.client;

import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface WeatherClient {

    <R> CompletableFuture<R> request(WeatherRequestParams params, Function<WeatherResponse, R> mapper);

}
