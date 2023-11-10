package com.example.RESTftulSN;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResTftulSnApplication {

	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");
		SpringApplication.run(ResTftulSnApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
