package com.trip.IronBird_Server.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DotenvConfig {

    private final Environment environment;

    @PostConstruct
    public void loaedENv(){
        Dotenv dotenv = Dotenv.configure().load();
        Map<String, Object> envVars = new HashMap<>();
        dotenv.entries().forEach(entry -> envVars.put(entry.getKey(), entry.getValue()));


        if(environment instanceof StandardEnvironment){

            StandardEnvironment standardEnvironment = (StandardEnvironment) environment;
            MutablePropertySources propertySources = standardEnvironment.getPropertySources();
            propertySources.addFirst(new MapPropertySource("dotenvProperties", envVars));
        }

    }

}
