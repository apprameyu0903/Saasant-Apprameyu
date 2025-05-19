package com.saasant.firstSpringProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstSpringProjectApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FirstSpringProjectApplication.class);

	public static void main(String[] args) {
		log.info("Starting FirstSpringProjectApplication...");
		SpringApplication.run(FirstSpringProjectApplication.class, args);
		log.info("FirstSpringProjectApplication has started successfully.");
	}
}
