package com.bank.management.config;

import com.bank.management.command.CreateUserCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerSubtypes(new NamedType(CreateUserCommand.class, "CreateUserCommand"));
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }

}
