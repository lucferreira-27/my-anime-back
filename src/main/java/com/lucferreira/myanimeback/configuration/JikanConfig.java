package com.lucferreira.myanimeback.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sandrohc.jikan.Jikan;

@Configuration
public class JikanConfig {

    @Bean
    public Jikan jikan() {
        return new Jikan();
    }
}
