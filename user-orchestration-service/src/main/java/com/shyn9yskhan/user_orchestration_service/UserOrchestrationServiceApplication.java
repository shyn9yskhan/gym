package com.shyn9yskhan.user_orchestration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserOrchestrationServiceApplication.class, args);
	}

}
