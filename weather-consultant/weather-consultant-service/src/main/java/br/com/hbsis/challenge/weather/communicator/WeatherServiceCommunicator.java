package br.com.hbsis.challenge.weather.communicator;

import br.com.hbsis.challenge.weather.WeatherConsultant;
import br.com.hbsis.challenge.weather.WeatherMessageConstants;
import br.com.hbsis.challenge.weather.WeatherRequestParams;
import br.com.hbsis.challenge.weather.WeatherResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceCommunicator {

    private final WeatherConsultant consultant;

    public WeatherServiceCommunicator(WeatherConsultant consultant) {
        this.consultant = consultant;
    }

    @RabbitListener(queues = WeatherMessageConstants.REQUEST_QUEUE)
    public WeatherResponse receive(WeatherRequestParams filter) {
        return consultant.find(filter);
    }

}
