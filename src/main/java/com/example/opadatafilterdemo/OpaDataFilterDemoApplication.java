package com.example.opadatafilterdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author joffryferrater
 */
@SpringBootApplication
public class OpaDataFilterDemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpaDataFilterDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(OpaDataFilterDemoApplication.class, args);
	}

}
