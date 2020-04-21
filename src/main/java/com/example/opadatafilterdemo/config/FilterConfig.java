package com.example.opadatafilterdemo.config;

import com.example.opadatafilterdemo.filter.XorgHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private static final String URL_PATTERN = "/pets";

    @Bean
    public FilterRegistrationBean<XorgHeaderFilter> loggingFilter() {
        FilterRegistrationBean<XorgHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XorgHeaderFilter());
        registrationBean.addUrlPatterns(URL_PATTERN);
        return registrationBean;

    }
}
