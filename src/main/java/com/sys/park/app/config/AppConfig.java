package com.sys.park.app.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    ModelMapper novoModelMapper() {
        return new ModelMapper() {
            @Override
            public org.modelmapper.config.Configuration getConfiguration() {
                return super.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            }
        };
    }
}

