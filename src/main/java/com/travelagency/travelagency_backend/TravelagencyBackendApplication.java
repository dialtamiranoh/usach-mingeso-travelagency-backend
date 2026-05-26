package com.travelagency.travelagency_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TravelagencyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelagencyBackendApplication.class, args);
	}

}
