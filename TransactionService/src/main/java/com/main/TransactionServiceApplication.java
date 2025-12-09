package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com")
@EnableMongoRepositories(basePackages = "com.repository")
public class TransactionServiceApplication {

	public static void main(String[] args) {

        SpringApplication.run(TransactionServiceApplication.class, args);
        System.out.println("Transaction Service Application started on port number 8082");

	}
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
