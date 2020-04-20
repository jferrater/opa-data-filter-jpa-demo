package com.example.opadatafilterdemo.config;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryFactoryBean;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        value = "com.example.opadatafilterdemo.repository",
        repositoryBaseClass = OpaRepositoryImpl.class,
        repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class
)
public class JpaEnvConfig {

}
