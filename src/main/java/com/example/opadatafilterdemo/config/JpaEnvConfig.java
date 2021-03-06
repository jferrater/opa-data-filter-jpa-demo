package com.example.opadatafilterdemo.config;

import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        value = "com.example.opadatafilterdemo.repository",
        repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class
)
public class JpaEnvConfig {

}
