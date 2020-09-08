package br.com.hbsis.challenge.weather.communicator;

import br.com.hbsis.challenge.weather.DefaultWeatherResponse;
import br.com.hbsis.challenge.weather.WeatherMessageConstants;
import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import br.com.hbsis.challenge.weather.client.WeatherClient;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
public class WeatherClientCommunicator implements WeatherClient {

    private final RabbitTemplate template;
    private final Exchange exchange;

    public WeatherClientCommunicator(RabbitTemplate template, Exchange exchange) {
        this.template = template;
        this.exchange = exchange;
    }

    @Async
    @Override
    public <R> CompletableFuture<R> request(WeatherRequestParams params, Function<WeatherResponse, R> mapper) {
        return CompletableFuture.supplyAsync(() -> mapper.apply(doRequest(params)));
    }

    private WeatherResponse doRequest(WeatherRequestParams params) {
        return template.convertSendAndReceiveAsType(exchange.getName(),
                WeatherMessageConstants.REQUEST_ROUTING_KEY,
                params,
                new ParameterizedTypeReference<DefaultWeatherResponse>() {
                });
    }

}
