package br.com.hbsis.challenge.weather.communicator;

import br.com.hbsis.challenge.weather.WeatherConsultant;
import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class WeatherServiceCommunicatorTest {

    private static final WeatherRequestParams VALID_PARAMS = getParams(35, 128);
    private static final WeatherResponse VALID_RESPONSE = getResponse("{}", null);
    private static final WeatherRequestParams INVALID_PARAMS = getParams(null, 128);
    private static final WeatherResponse INVALID_RESPONSE = getResponse(null, "{}");

    @Mock
    WeatherConsultant consultant = mock(WeatherConsultant.class);

    @Before
    public void beforeTest() {
        when(consultant.find(VALID_PARAMS)).thenReturn(VALID_RESPONSE);
        when(consultant.find(INVALID_PARAMS)).thenReturn(INVALID_RESPONSE);
    }

    @Test
    public void whenValidRequestThenResponse() {

        final WeatherServiceCommunicator communicator = new WeatherServiceCommunicator(consultant);
        final WeatherResponse weatherResponse = communicator.receive(VALID_PARAMS);

        assertEquals(weatherResponse.getData(), VALID_RESPONSE.getData());
        assertNull(weatherResponse.getError());
    }

    @Test
    public void whenInvalidRequestThenResponse() {

        final WeatherServiceCommunicator communicator = new WeatherServiceCommunicator(consultant);
        final WeatherResponse weatherResponse = communicator.receive(INVALID_PARAMS);

        assertEquals(weatherResponse.getError(), INVALID_RESPONSE.getError());
        assertNull(weatherResponse.getData());
    }

    private static WeatherRequestParams getParams(Integer lat, Integer lon) {
        return new WeatherRequestParams() {
            @Override
            public Integer getLat() {
                return lat;
            }

            @Override
            public Integer getLon() {
                return lon;
            }
        };
    }

    private static WeatherResponse getResponse(String data, String error) {
        return new WeatherResponse() {
            @Override
            public String getData() {
                return data;
            }

            @Override
            public String getError() {
                return error;
            }
        };
    }
}
