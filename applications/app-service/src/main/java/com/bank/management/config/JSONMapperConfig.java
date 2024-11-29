package com.bank.management.config;

import com.bank.management.JSONMapper;
import com.bank.management.JSONMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONMapperConfig {

    @Bean
    public JSONMapper jsonMapper() {
        return new JSONMapperImpl();
    }
}
