package br.com.hbsis.challenge.weather.config;

import br.com.hbsis.challenge.weather.WeatherConsultant;
import br.com.hbsis.challenge.weather.WeatherMessageConstants;
import br.com.hbsis.challenge.weather.api.APIOpenWeather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.util.ErrorHandler;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Configuration
@EnableRabbit
public class WeatherServiceConfiguration {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(WeatherMessageConstants.EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(WeatherMessageConstants.REQUEST_QUEUE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(directExchange())
                .with(WeatherMessageConstants.REQUEST_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory =
                new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory);
        listenerContainerFactory.setMessageConverter(jackson2MessageConverter());
        listenerContainerFactory.setDefaultRequeueRejected(false);
        listenerContainerFactory.setMaxConcurrentConsumers(10);
        listenerContainerFactory.setMaxConcurrentConsumers(100);
        return listenerContainerFactory;
    }

    @Bean
    public WeatherConsultant weatherConsultant(){
        return new APIOpenWeather(new RestTemplate(), "http://api.openweathermap.org/data/2.5/weather", "eb8b1a9405e659b2ffc78f0a520b1a46");
    }
}
