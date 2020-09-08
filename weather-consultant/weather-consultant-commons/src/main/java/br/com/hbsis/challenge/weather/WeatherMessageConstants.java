package br.com.hbsis.challenge.weather;

public final class WeatherMessageConstants {

    public static final String EXCHANGE = "hbis.weather";
    public static final String REQUEST_QUEUE = "hbis.weather.request";
    public static final String REQUEST_ROUTING_KEY = REQUEST_QUEUE;

    private WeatherMessageConstants(){
        //Utility class
    }
}
