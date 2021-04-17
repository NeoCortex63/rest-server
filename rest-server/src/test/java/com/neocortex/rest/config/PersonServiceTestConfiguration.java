package com.neocortex.rest.config;

import com.neocortex.rest.service.PersonService;
import com.neocortex.rest.service.PersonServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PersonServiceTestConfiguration {
    @Bean
    public PersonService personService() {
        return new PersonServiceImpl();
    }
}
