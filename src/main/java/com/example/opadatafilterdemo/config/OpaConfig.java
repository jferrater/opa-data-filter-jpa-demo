package com.example.opadatafilterdemo.config;

import com.example.opadatafilterdemo.service.PartialRequestGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class OpaConfig {

    @Bean
    @RequestScope
    @Qualifier("partialRequestGenerator")
    public PartialRequestGenerator partialRequestGenerator() {
        System.out.println("Here na pud mi again!");
        return new PartialRequestGenerator();
    }
}
