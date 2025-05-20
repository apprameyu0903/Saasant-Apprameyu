package com.saasant.firstSpringProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.UUID;

@SpringBootApplication
public class FirstSpringProjectApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FirstSpringProjectApplication.class);
    public static final String APPLICATION_INSTANCE_ID = UUID.randomUUID().toString();
    private static final String LOGGING_ID_KEY = "transactionId";

	public static void main(String[] args) {
		MDC.put(LOGGING_ID_KEY, APPLICATION_INSTANCE_ID);

        log.info("Starting FirstSpringProjectApplication with Instance ID: {}", APPLICATION_INSTANCE_ID);
		SpringApplication.run(FirstSpringProjectApplication.class, args);
		log.info("FirstSpringProjectApplication has started successfully with Instance ID: {}", APPLICATION_INSTANCE_ID);
	}
}
