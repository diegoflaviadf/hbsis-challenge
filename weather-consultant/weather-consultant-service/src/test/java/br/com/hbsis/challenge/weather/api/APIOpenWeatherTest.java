package br.com.hbsis.challenge.weather.api;

import br.com.hbsis.challenge.weather.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

import static br.com.hbsis.challenge.weather.api.APIOpenWeather.API_KEY_PARAM_NAME;
import static br.com.hbsis.challenge.weather.api.APIOpenWeather.CLIENT_EXCEPTION_KEY_MSG;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherConsultantApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class APIOpenWeatherTest {

    private static final String API_KEY = "apiKey";
    private static final String BASE_URL = "http://api.teste.com";
    private static final WeatherRequestParams PARAM = DefaultWeatherRequestParams.builder().lat(35).lon(128).build();
    private static final String URL = "http://api.teste.com?lat=35&lon=128&appid=apiKey";

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private APIOpenWeather apiOpenWeather = new APIOpenWeather(restTemplate, BASE_URL, API_KEY);

    @Test
    public void whenFindValid() {
        final String body = "{}";

        when(restTemplate.getForEntity(URL, String.class)).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        final WeatherResponse response = apiOpenWeather.find(PARAM);
        assertEquals(body, response.getData());
        assertNull(response.getError());
    }

    @Test
    public void whenFindInvalid() {
        when(restTemplate.getForEntity(URL, String.class)).thenThrow(new RestClientException("rest client error"));

        final WeatherResponse response = apiOpenWeather.find(PARAM);
        assertNull(response.getData());
        assertEquals(response.getError(), CLIENT_EXCEPTION_KEY_MSG);
    }

    @Test
    public void whenDoRequest() {
        final String body = "{}";

        when(restTemplate.getForEntity(URL, String.class)).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        final WeatherResponse response = apiOpenWeather.doRequest(PARAM);
        assertEquals(body, response.getData());
    }

    @Test
    public void whenBuildURI() {
        String uri = apiOpenWeather.buildURI(PARAM);

        assertEquals(uri, URL);
    }

    @Test
    public void whenBuildQueryParams() {
        MultiValueMap<String, String> convertedParams = apiOpenWeather.buildQueryParams(PARAM);

        assertEquals(convertedParams.get("lat").get(0), PARAM.getLat().toString());
        assertEquals(convertedParams.get("lon").get(0), PARAM.getLon().toString());
        assertEquals(convertedParams.get(API_KEY_PARAM_NAME).get(0), API_KEY);
    }

    @Test
    public void whenConvertParamsMap() {
        MultiValueMap<String, String> convertedParams = apiOpenWeather.convertParamsToMap(PARAM);

        assertEquals(convertedParams.get("lat").get(0), PARAM.getLat().toString());
        assertEquals(convertedParams.get("lon").get(0), PARAM.getLon().toString());
    }

    @Test
    public void whenConvertParamsToMultiValueMap() {
        Map<String, Object> attributes = new LinkedHashMap<>();
        MultiValueMap<String, String> convertedParams;

        final String param1 = "param1";
        attributes.put(param1, "value1");
        convertedParams = apiOpenWeather.convertParamsToMultiValueMap(attributes);
        assertEquals(convertedParams.size(), 1);

        final String param2 = "param2";
        attributes.put(param2, 2);
        convertedParams = apiOpenWeather.convertParamsToMultiValueMap(attributes);
        assertEquals(convertedParams.size(), 2);

        assertEquals(convertedParams.get(param1).get(0), attributes.get(param1).toString());
        assertEquals(convertedParams.get(param2).get(0), attributes.get(param2).toString());
    }

    @Test
    public void whenAddApiKey() {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        apiOpenWeather.addApiKey(params);
        assertTrue(params.containsKey(API_KEY_PARAM_NAME));
        assertEquals(params.get(API_KEY_PARAM_NAME).get(0), API_KEY);
    }
}
