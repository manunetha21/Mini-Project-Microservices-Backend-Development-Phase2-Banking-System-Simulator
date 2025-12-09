package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com")
@EnableDiscoveryClient
public class NotificationServiceApplication {

	public static void main(String[] args) {

        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("Notification Service Application started on port number on 8083");
	}

}
