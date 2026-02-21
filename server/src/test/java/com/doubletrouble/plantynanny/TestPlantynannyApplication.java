package com.doubletrouble.plantynanny;

import org.springframework.boot.SpringApplication;

public class TestPlantynannyApplication {

	public static void main(String[] args) {
		SpringApplication.from(PlantyNannyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
