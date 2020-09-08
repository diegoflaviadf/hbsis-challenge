package br.com.hbsis.challenge.weather.rules;

import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import br.com.hbsis.challenge.weather.client.WeatherClient;
import br.com.hbsis.challenge.weather.dto.WeatherDTO;
import br.com.hbsis.challenge.weather.dto.WeatherRegistrationDTO;
import br.com.hbsis.challenge.weather.mapper.WeatherRegistrationMapper;
import org.springframework.stereotype.Service;
import br.com.hbsis.challenge.weather.weather.model.Weather;
import br.com.hbsis.challenge.weather.weather.repositories.WeatherRepository;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class WeatherRule {

    private final WeatherClient client;
    private final WeatherRegistrationMapper mapper;
    private final WeatherRepository repository;

    public WeatherRule(WeatherClient client, WeatherRegistrationMapper mapper, WeatherRepository repository) {
        this.client = client;
        this.mapper = mapper;
        this.repository = repository;
    }

    public CompletableFuture<WeatherDTO> register(WeatherRegistrationDTO dto) {
        return consultWeather(dto, this::persist);
    }

    private CompletableFuture<WeatherDTO> consultWeather(WeatherRegistrationDTO dto, Function<WeatherResponse, WeatherDTO> action) {
        final WeatherRequestParams params = mapper.mapRequest(dto);
        return client.request(params, action);
    }

    private WeatherDTO persist(WeatherResponse response) {
        final Weather savedWeather = repository.save(mapper.mapToEntity(response));
        return mapper.mapToDTO(savedWeather);
    }

}
