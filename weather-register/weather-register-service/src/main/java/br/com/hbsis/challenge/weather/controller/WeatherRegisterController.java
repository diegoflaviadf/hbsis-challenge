package br.com.hbsis.challenge.weather.controller;

import br.com.hbsis.challenge.weather.dto.WeatherRegistrationDTO;
import br.com.hbsis.challenge.weather.dto.WeatherDTO;
import br.com.hbsis.challenge.weather.rules.WeatherRule;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class WeatherRegisterController {

    private final WeatherRule rule;

    public WeatherRegisterController(WeatherRule rule) {
        this.rule = rule;
    }

    @PostMapping("/weather")
    @ResponseBody
    public CompletableFuture<WeatherDTO> register(@RequestBody @Valid WeatherRegistrationDTO dto) {
        return rule.register(dto);
    }

}
