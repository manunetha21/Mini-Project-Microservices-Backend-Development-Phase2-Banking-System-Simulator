package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com")
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("API Gateway Application Started on port 8080");
	}

}
