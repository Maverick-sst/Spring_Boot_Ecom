package com.example.ecommerce.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvPropertySource implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        Map<String, Object> dotenvMap = new HashMap<>();
        dotenv.entries().forEach(entry -> 
            dotenvMap.put(entry.getKey(), entry.getValue())
        );
        
        applicationContext.getEnvironment()
            .getPropertySources()
            .addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
    }
}