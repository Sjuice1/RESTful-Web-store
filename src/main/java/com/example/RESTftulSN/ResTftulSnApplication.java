package com.example.RESTftulSN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResTftulSnApplication {

	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");
		SpringApplication.run(ResTftulSnApplication.class, args);
	}

}
