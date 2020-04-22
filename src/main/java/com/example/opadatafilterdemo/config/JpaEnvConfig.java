package com.example.opadatafilterdemo.config;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        value = "com.example.opadatafilterdemo.repository"
        ,repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class
)
@EntityScan("com.example.opadatafilterdemo.repository")
public class JpaEnvConfig {

}
