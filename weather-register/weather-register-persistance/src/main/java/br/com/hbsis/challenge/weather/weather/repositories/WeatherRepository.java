package br.com.hbsis.challenge.weather.weather.repositories;

import br.com.hbsis.challenge.weather.weather.model.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, UUID> {

}
