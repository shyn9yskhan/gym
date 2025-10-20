package com.shyn9yskhan.training_orchestration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TrainingOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingOrchestrationServiceApplication.class, args);
	}

}
