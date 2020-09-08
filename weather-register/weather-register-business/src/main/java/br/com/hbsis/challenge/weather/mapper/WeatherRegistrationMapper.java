package br.com.hbsis.challenge.weather.mapper;

import br.com.hbsis.challenge.weather.DefaultWeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import br.com.hbsis.challenge.weather.dto.WeatherDTO;
import br.com.hbsis.challenge.utils.DefaultMapper;
import br.com.hbsis.challenge.weather.dto.WeatherRegistrationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import br.com.hbsis.challenge.weather.rules.exception.BusinessException;
import br.com.hbsis.challenge.weather.weather.model.Weather;

import java.util.Objects;

@Component
public class WeatherRegistrationMapper {

    public static final String WEATHER_REQUEST_ERROR = "weather.request.error";
    public static final String WEATHER_RESPONSE_ERROR = "weather.response.error";

    public WeatherRequestParams mapRequest(WeatherRegistrationDTO dto) {
        if (Objects.isNull(dto))
            throw new BusinessException(WEATHER_RESPONSE_ERROR);
        return DefaultWeatherRequestParams.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public Weather mapToEntity(WeatherResponse response) {
        if (Objects.isNull(response))
            throw new BusinessException(WEATHER_RESPONSE_ERROR);
        try {
            if (Objects.nonNull(response.getData())) {
                final Weather.Attributes attributes = DefaultMapper.INSTANCE.readValue(response.getData(), Weather.Attributes.class);
                return new Weather(attributes);
            }
            throw new BusinessException(response.getError());
        } catch (JsonProcessingException e) {
            throw new BusinessException(WEATHER_RESPONSE_ERROR);
        }
    }

    public WeatherDTO mapToDTO(Weather entity) {
        if (Objects.isNull(entity))
            throw new BusinessException(WEATHER_RESPONSE_ERROR);
        final WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setId(entity.getId());
        weatherDTO.setCreatedAt(entity.getDtCreatedAt());
        weatherDTO.setAttrs(entity.getAttributes());
        return weatherDTO;
    }

}
