package br.com.hbsis.challenge.weather.api;

import br.com.hbsis.challenge.utils.DefaultMapper;
import br.com.hbsis.challenge.weather.DefaultWeatherResponse;
import br.com.hbsis.challenge.weather.WeatherConsultant;
import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
public class APIOpenWeather implements WeatherConsultant {

    public static final String API_KEY_PARAM_NAME = "appid";
    public static final String CLIENT_EXCEPTION_KEY_MSG = "weather.request.error";

    public final String url;
    private final String apiKey;
    private final RestTemplate restTemplate;

    public APIOpenWeather(RestTemplate restTemplate, String url, String apiKey) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.apiKey = apiKey;
    }

    @Override
    public WeatherResponse find(WeatherRequestParams params) {
        try {
            return doRequest(params);
        } catch (RestClientException exception) {
            log.error(exception.getMessage(), exception);
            return DefaultWeatherResponse.builder().error(CLIENT_EXCEPTION_KEY_MSG).build();
        }
    }

    DefaultWeatherResponse doRequest(WeatherRequestParams params) {
        String uri = buildURI(params);
        ResponseEntity<String> response
                = restTemplate.getForEntity(uri, String.class);
        return DefaultWeatherResponse.builder().data(response.getBody()).build();
    }

    String buildURI(WeatherRequestParams params) {
        final MultiValueMap<String, String> newAttributes = buildQueryParams(params);
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(newAttributes)
                .toUriString();
    }

    MultiValueMap<String, String> buildQueryParams(WeatherRequestParams params) {
        final MultiValueMap<String, String> mappedParams = convertParamsToMap(params);
        addApiKey(mappedParams);
        return mappedParams;
    }

    MultiValueMap<String, String> convertParamsToMap(WeatherRequestParams params) {
        final Map<String, Object> attributes = DefaultMapper.INSTANCE.convertValue(params, Map.class);
        return convertParamsToMultiValueMap(attributes);
    }

    MultiValueMap<String, String> convertParamsToMultiValueMap(Map<String, Object> attributes) {
        final MultiValueMap<String, String> newAttributes = new LinkedMultiValueMap<>(attributes.size());
        attributes.forEach((key, value) -> newAttributes.add(key, value.toString()));
        return newAttributes;
    }

    void addApiKey(MultiValueMap<String, String> attributes) {
        attributes.add(API_KEY_PARAM_NAME, apiKey);
    }
}
