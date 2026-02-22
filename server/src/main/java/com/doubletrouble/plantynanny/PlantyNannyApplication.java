package com.doubletrouble.plantynanny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantyNannyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantyNannyApplication.class, args);
	}

}
